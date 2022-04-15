package org.caffeine.chaos.api.handlers

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.*
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
    try {
    val d = Json { ignoreUnknownKeys = true }.decodeFromString<messageCreate>(payload).d
        val messageauthor = MessageAuthor(
            d.author.username,
            d.author.discriminator,
            d.author.id,
            d.author.avatar
        )
        val messagechannel = MessageChannel(d.channel_id)
        val message = Message(d.id, messageauthor, d.content, messagechannel)
    val event = MessageCreateEvent(message)
        event.message.content = d.content
        event.message.id = d.id
        commandHandler(config, event, client)
    }catch (e: Exception){
        println("you are ugly")
        println(e)
        e.printStackTrace()
    }
}