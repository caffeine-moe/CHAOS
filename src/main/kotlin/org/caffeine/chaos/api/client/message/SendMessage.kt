package org.caffeine.chaos.api.client.message

import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
private data class SendMessageResponse(
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
    val referenced_message : SendMessageResponse? = null,
    val timestamp : String?,
    val tts : Boolean?,
    val type : Int?,
)

suspend fun sendMessage(channel : MessageChannel, message : Message) : CompletableFuture<Message> {
/*    if (message.content.length > 2000) {
        log("Unable to send message as it is over 2000 characters in length.", "API:")
        return CompletableFuture.failedFuture(Throwable("CONTENT_TOO_LONG"))
    } else {
        val response = client.request("$BASE_URL/channels/${channel.id}/messages") {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Authorization, client.user.token)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(
                json.encodeToString(
                    MessageSerializer(
                        message.content,
                        calcNonce(),
                    ),
                ),
            )
        }

        val parsedResponse = json.decodeFromString<SendMessageResponse>(response.body())
        val messageAuthor = MessageAuthor(
            parsedResponse.author.username,
            parsedResponse.author.discriminator,
            parsedResponse.author.id,
            parsedResponse.author.avatar
        )
        val sentMessage = Message(parsedResponse.id, parsedResponse.content, parsedResponse.channel_id, messageAuthor)

        return CompletableFuture.completedFuture(sentMessage)
    }   */
    return CompletableFuture.completedFuture(message)
}