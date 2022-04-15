package org.caffeine.chaos.api.client.message

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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
        val response = httpclient.request("$BASE_URL/channels/${message.channel.id}/messages/${message.id}") {
            method = HttpMethod.Patch
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(Json.encodeToString(editContent(newMessage.content)))
        }
        val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<editMessageResponse>(response.body())
    val messageauthor =  MessageAuthor(
        parsedresponse.author.username,
        parsedresponse.author.discriminator,
        parsedresponse.author.id,
        parsedresponse.author.avatar
    )
    val messagechannel = MessageChannel(parsedresponse.channel_id)
    val editedmessage = Message(parsedresponse.id, messageauthor, parsedresponse.content, messagechannel)
    return CompletableFuture.completedFuture(editedmessage)
}