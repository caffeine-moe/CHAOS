package org.caffeine.chaos.api.client.message

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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
        val collection: MutableCollection<Message> = mutableListOf()
        val response = httpclient.request("$BASE_URL/channels/${this.id}/messages?limit=100") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
        }
        val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<List<Message>>(response.body())
        for (msg: Message in parsedresponse) {
            collection.add(msg)
        }
        return collection.stream()
    }

    suspend fun sendMessage(message: Message, config: Config, client: Client): CompletableFuture<Message> {
        return client.sendMessage(this, message, config)
    }
}