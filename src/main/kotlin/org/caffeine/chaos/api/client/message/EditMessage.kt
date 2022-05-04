package org.caffeine.chaos.api.client.message

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
private data class EditMessageResponse(
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
    val referenced_message: EditMessageResponse? = null,
    val timestamp: String?,
    val tts: Boolean?,
    val type: Int?,
)

@kotlinx.serialization.Serializable
data class EditContent(
    val content: String,
)

suspend fun editMessage(message: Message, client: Client, newMessage: Message): CompletableFuture<Message> {
    val response = discordHTTPClient.request("$BASE_URL/channels/${message.channel_id}/messages/${message.id}") {
        method = HttpMethod.Patch
        headers {
            append(HttpHeaders.Authorization, client.config.token)
            append(HttpHeaders.ContentType, "application/json")
        }
        setBody(json.encodeToString(EditContent(newMessage.content)))
    }
    val parsedresponse = json.decodeFromString<EditMessageResponse>(response.body())
    val messageauthor = MessageAuthor(
        parsedresponse.author.username,
        parsedresponse.author.discriminator,
        parsedresponse.author.id,
        parsedresponse.author.avatar
    )
    val editedmessage = Message(parsedresponse.id, parsedresponse.content, parsedresponse.channel_id, messageauthor)
    return CompletableFuture.completedFuture(editedmessage)
}