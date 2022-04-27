package org.caffeine.chaos.api.client.message

import org.caffeine.chaos.api.client.Client
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
class Message(
    val id: String?,
    val content: String = "",
    val channel_id: String = "",
    val author: MessageAuthor = MessageAuthor("", "", "", ""),
    val attachments: List<MessageAttachment>? = null,
    val embeds: List<MessageEmbed>? = null,
    val mention_everyone: Boolean? = false,
    val mention_roles: List<String>? = null,
    val mentions: List<MessageMention>? = null,
    val pinned: Boolean? = false,
    val referenced_message: Message? = null,
) {
    //val mentionedUsers: String
    suspend fun edit(message: Message, client: Client): CompletableFuture<Message> {
        return editMessage(this, client, message)
    }

    suspend fun delete(client: Client) {
        try {
            deleteMessage(this, client)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}