package org.caffeine.chaos.api.client.message

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.config.Config
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream

@kotlinx.serialization.Serializable
class MessageChannel(var id: String) {

    suspend fun messagesAsStream(config: Config): Stream<Message> {
        val collection: MutableList<Message> = mutableListOf()
        val messagesPerRequest = 100
        val filters = (MessageFilters(null,null,null,null,null))
        while (true) {
            var parameters = ""
            parameters += if (filters.limit != null) "limit=${messagesPerRequest.coerceAtMost(filters.limit!! - collection.size)}&"
            else "limit=${messagesPerRequest}&"
            if (filters.before_id != null) parameters += "before=${filters.before_id}&"
            if (filters.after_id != null) parameters += "after=${filters.author_id}&"
            if (filters.author_id != null) parameters += "author_id=${filters.author_id}&"
            if (filters.mentioning_user_id != null) parameters += "mentions=${filters.mentioning_user_id}&"
            val response = httpclient.request("$BASE_URL/channels/${this.id}/messages?${parameters}") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, config.token)
                    append(HttpHeaders.ContentType, "application/json")
                }
            }
            val newMessages = Json { ignoreUnknownKeys = true }.decodeFromString<List<Message>>(response.body())
            collection.addAll(newMessages)

            filters.before_id = collection.last().id?.toLong()

            if (newMessages.size < messagesPerRequest)
                break

            withContext(Dispatchers.IO) {
                Thread.sleep(500)
            }
        }
        return collection.stream()
    }

    suspend fun sendMessage(message: Message, config: Config, client: Client): CompletableFuture<Message> {
        return client.sendMessage(this, message, config)
    }
}