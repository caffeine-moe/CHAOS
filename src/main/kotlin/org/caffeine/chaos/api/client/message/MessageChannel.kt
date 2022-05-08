package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json
import java.util.concurrent.CompletableFuture

class MessageChannel(var id: String, var client: Client) {

    suspend fun messagesAsCollection(filters: MessageFilters): Collection<Message> {
        val collection: MutableList<Message> = mutableListOf()
        val messagesPerRequest = 100
        while (true) {
            var parameters = ""
            parameters += if (filters.limit > 0) "limit=${messagesPerRequest.coerceAtMost(filters.limit - collection.size)}&"
            else "limit=${messagesPerRequest}&"
            if (filters.before_id.isNotBlank()) parameters += "before=${filters.before_id}&"
            if (filters.after_id.isNotBlank()) parameters += "after=${filters.after_id}&"
            if (filters.author_id.isNotBlank()) parameters += "author_id=${filters.author_id}&"
            if (filters.mentioning_user_id.isNotBlank()) parameters += "mentions=${filters.mentioning_user_id}&"
            val response = discordHTTPClient.request("$BASE_URL/channels/${this.id}/messages?${parameters}") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, client.config.token)
                    append(HttpHeaders.ContentType, "application/json")
                }
            }
            val newMessages = json.decodeFromString<List<Message>>(response.bodyAsText())
            collection.addAll(newMessages)
            filters.before_id = collection.last().id.toString()
            collection.removeIf { it.author.id != filters.author_id }

            if (filters.needed != 0 && collection.size >= filters.needed)
                break

            if (newMessages.size < messagesPerRequest)
                break
        }
        return collection
    }

    suspend fun sendMessage(message: Message, client: Client): CompletableFuture<Message> {
        return client.user.sendMessage(this, message)
    }
}