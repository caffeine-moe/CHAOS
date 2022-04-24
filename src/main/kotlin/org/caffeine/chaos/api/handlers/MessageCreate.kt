package org.caffeine.chaos.api.handlers

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.*
import org.caffeine.chaos.commandHandler

@Serializable
private data class MessageCreate(
    val d: MessageCreateD,
    val op: Int?,
    val s: Int?,
    val t: String?,
)

@Serializable
private data class MessageCreateD(
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
    val pinned: Boolean?,
    val referenced_message: MessageCreateD? = null,
    val timestamp: String?,
    val tts: Boolean?,
    val type: Int?,
)

suspend fun messageCreate(payload: String, config: Config, client: Client) {
    try {
        val d = Json { ignoreUnknownKeys = true }.decodeFromString<MessageCreate>(payload).d
        val messageauthor = MessageAuthor(
            d.author.username,
            d.author.discriminator,
            d.author.id,
            d.author.avatar
        )
        val message = Message(d.id, d.content, d.channel_id, messageauthor, d.attachments)
        val event = MessageCreateEvent(message, MessageChannel(d.channel_id))
        commandHandler(config, event, client)
    } catch (e: Exception) {
        println(payload)
        println("you are ugly")
        println(e)
        println(e.cause)
        e.printStackTrace()
    }
}