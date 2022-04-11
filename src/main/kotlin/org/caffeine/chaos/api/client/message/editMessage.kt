package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.api.response
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
private data class editMessageResponse(
    val attachments: List<String>,
    val author: MessageAuthor,
    val channel_id: String,
    val components: List<String>,
    val content: String,
    val edited_timestamp: String?,
    val embeds: List<String>,
    val flags: Int,
    val id: String,
    val mention_everyone: Boolean,
    val mention_roles: List<String>,
    val mentions: List<String>,
    val pinned: Boolean,
    val timestamp: String,
    val tts: Boolean,
    val type: Int
)

@kotlinx.serialization.Serializable
data class editContent(
    val content: String
)

suspend fun editMessage(message: Message, config: Config, newMessage: Message) : CompletableFuture<Message>{
        val response = httpclient.request<String>("$BASE_URL/channels/${message.channel.id}/messages/${message.id}") {
            method = HttpMethod.Patch
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
            body = Json.encodeToJsonElement(editContent(newMessage.content))
        }
        val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<editMessageResponse>(response)
        val returnMessage = Message()
    try {
        returnMessage.author = MessageAuthor(
            parsedresponse.author.username,
            parsedresponse.author.discriminator,
            parsedresponse.author.id,
            parsedresponse.author.avatar
        )
        returnMessage.channel.id = parsedresponse.channel_id
        returnMessage.content = parsedresponse.content
        returnMessage.id = parsedresponse.id
    }catch (e: Exception){
        e.printStackTrace()
    }
    return CompletableFuture.completedFuture(returnMessage)
}