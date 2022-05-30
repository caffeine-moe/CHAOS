package org.caffeine.chaos.api.handlers

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.*
import org.caffeine.chaos.api.json
import org.caffeine.chaos.handleMessage

@Serializable
private data class MessageCreate(
    val d: MessageCreateD,
    val op: Int?,
    val s: Int?,
    val t: String?,
)

@Serializable
private data class MessageCreateD(
    val attachments: List<MessageAttachment> = mutableListOf(),
    val author: MessageAuthor,
    val channel_id: String,
    val content: String,
    val edited_timestamp: String?,
    val flags: Int?,
    val id: String,
    val mention_everyone: Boolean = false,
    val mention_roles: List<String> = mutableListOf(),
    val mentions: List<MessageMention> = mutableListOf(),
    val pinned: Boolean,
    val referenced_message: MessageCreateD? = null,
    val timestamp: String?,
    val tts: Boolean?,
    val type: Int,
)

suspend fun messageCreate(payload: String, client: Client) {
    val d = json.decodeFromString<MessageCreate>(payload).d
    val messageAuthor = MessageAuthor(
        d.author.username,
        d.author.discriminator,
        d.author.id,
        d.author.avatar
    )
    val message = Message(
        d.id,
        d.content,
        d.channel_id,
        messageAuthor,
        d.attachments,
        mentions = d.mentions,
        pinned = d.pinned
    )
    if (d.referenced_message != null) {
        message.referenced_message = createReferencedMessage(d.referenced_message)
    }
    val event = MessageCreateEvent(message, client, MessageChannel(d.channel_id))
    handleMessage(event, client)
}

private fun createReferencedMessage(ref: MessageCreateD) : Message {
    val refAuthor = MessageAuthor(
        ref.author.username,
        ref.author.discriminator,
        ref.author.id,
        ref.author.avatar
    )
    val message = Message(
        ref.id,
        ref.content,
        ref.channel_id,
        refAuthor,
        ref.attachments,
        mentions = ref.mentions,
        pinned = ref.pinned
    )
    if (ref.referenced_message != null) {
        message.referenced_message = createReferencedMessage(ref.referenced_message)
    }
    return message
}