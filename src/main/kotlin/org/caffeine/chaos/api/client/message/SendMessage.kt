package org.caffeine.chaos.api.client.message

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.config.Config
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
private data class SendMessageResponse(
    val attachments: List<MessageAttachment>?,
    val author: MessageAuthor,
    val channel_id: String,
    val content: String,
    val edited_timestamp: String?,
    val flags: Int?,
    val id: String,
    val mention_everyone: Boolean?,
    val mention_roles: List<String>?,
    val mentions: List<MessageMention>?,
    val nonce: String? = null,
    val pinned: Boolean?,
    val referenced_message: SendMessageResponse? = null,
    val timestamp: String?,
    val tts: Boolean?,
    val type: Int?,
)

suspend fun sendMessage(channel: MessageChannel, message: Message, config: Config): CompletableFuture<Message> {
    val response = httpclient.request("$BASE_URL/channels/${channel.id}/messages") {
        method = HttpMethod.Post
        headers {
            append(HttpHeaders.Authorization, config.token)
            append(HttpHeaders.ContentType, "application/json")
        }
        setBody(Json.encodeToString(MessageSerializer(message.content,
            System.currentTimeMillis().toString())))
    }
    val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<SendMessageResponse>(response.body())
    val messageauthor = MessageAuthor(
        parsedresponse.author.username,
        parsedresponse.author.discriminator,
        parsedresponse.author.id,
        parsedresponse.author.avatar
    )
    val sentmessage = Message(parsedresponse.id, parsedresponse.content, parsedresponse.channel_id, messageauthor)
    return CompletableFuture.completedFuture(sentmessage)
}