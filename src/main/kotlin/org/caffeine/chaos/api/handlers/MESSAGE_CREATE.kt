package org.caffeine.chaos.api.handlers

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageAuthor
import org.caffeine.chaos.api.client.message.MessageChannel
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commandHandler

@Serializable
private data class messageCreate(
    val d: D,
    val op: Int?,
    val s: Int?,
    val t: String?
)

@Serializable
private data class D(
    val attachments: List<String>?,
    val author: Author,
    val channel_id: String,
    val components: List<String>?,
    val content: String,
    val edited_timestamp: String?,
    val embeds: List<String>?,
    val flags: Int?,
    val id: String,
    val mention_everyone: Boolean?,
    val mention_roles: List<String>?,
    val mentions: List<String>?,
    val nonce: String?,
    val pinned: Boolean?,
    val referenced_message: String?,
    val timestamp: String?,
    val tts: Boolean?,
    val type: Int?
)

@Serializable
private data class Author(
    val avatar: String?,
    val discriminator: Int,
    val id: String,
    val username: String
)


suspend fun messagecreate(payload: String, config: Config, client: Client){
    val d = Json { ignoreUnknownKeys = true }.decodeFromString<messageCreate>(payload).d
    val event = MessageCreateEvent()
    try {
        event.message.content = d.content
        event.message.id = d.id
        event.message.author = MessageAuthor(
            d.author.username,
            d.author.discriminator,
            d.author.id,
            d.author.avatar
        )
        event.message.channel.id = d.channel_id
    }catch (e: Exception){
        println("you are ugly")
        println(e)
        println(e.stackTrace.toString())
    }
    commandHandler(config, event, client)
}