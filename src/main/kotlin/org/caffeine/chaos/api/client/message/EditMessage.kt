package org.caffeine.chaos.api.client.message

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.json
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
private data class EditMessageResponse(
    val attachments : List<MessageAttachment>?,
    val author : MessageAuthor,
    val channel_id : String,
    val content : String,
    val edited_timestamp : String?,
    val flags : Int?,
    val id : String,
    val mention_everyone : Boolean?,
    val mention_roles : List<String>?,
    val mentions : List<MessageMention>?,
    val nonce : String? = null,
    val pinned : Boolean?,
    val referenced_message : EditMessageResponse? = null,
    val timestamp : String?,
    val tts : Boolean?,
    val type : Int,
)

@kotlinx.serialization.Serializable
data class EditContent(
    val content : String,
)

/*
suspend fun editMessage(message : Message, newMessage : Message) : CompletableFuture<Message> {
    val response = discordHTTPClient.request("$BASE_URL/channels/${message.channel_id}/messages/${message.id}") {
        method = HttpMethod.Patch
        headers {
            append(HttpHeaders.Authorization, client.user.token)
            append(HttpHeaders.ContentType, "application/json")
        }
        setBody(json.encodeToString(EditContent(newMessage.content)))
    }
    val parsedResponse = json.decodeFromString<EditMessageResponse>(response.body())
    val messageAuthor = MessageAuthor(
        parsedResponse.author.username,
        parsedResponse.author.discriminator,
        parsedResponse.author.id,
        parsedResponse.author.avatar
    )
    val editedMessage = Message(parsedResponse.id,
        parsedResponse.content,
        parsedResponse.channel_id,
        messageAuthor,
        type = parsedResponse.type)
    return CompletableFuture.completedFuture(editedMessage)
}*/
