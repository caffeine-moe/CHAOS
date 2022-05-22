package org.caffeine.chaos.api.client.message

import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
class Message(
    val id: String?,
    val content: String = "",
    val channel_id: String = "",
    val author: MessageAuthor = MessageAuthor("", "", "", ""),
    val attachments: List<MessageAttachment> = mutableListOf(),
    val embeds: List<MessageEmbed>? = null,
    val mention_everyone: Boolean? = false,
    val mention_roles: List<String>? = null,
    val mentions: List<MessageMention> = mutableListOf(),
    val pinned: Boolean? = false,
    val referenced_message: Message? = null,
    val type: Int = 0,
) {
    suspend fun edit(message: Message): CompletableFuture<Message> {
        return editMessage(this, message)
    }

    suspend fun delete() {
        try {
            deleteMessage(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}