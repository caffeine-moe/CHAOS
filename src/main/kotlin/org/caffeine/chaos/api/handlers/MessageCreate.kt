package org.caffeine.chaos.api.handlers

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.EventBus
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.models.Message
import org.caffeine.chaos.api.models.channels.TextChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser

@Serializable
private data class MessageCreate(
    val d : MessageCreateD,
    val op : Int?,
    val s : Int?,
    val t : String?,
)

@Serializable
private data class MessageCreateD(
    //val attachments : List<MessageAttachment> = mutableListOf(),
    val author : User,
    val channel_id : String,
    val content : String,
    val edited_timestamp : String?,
    val flags : Int?,
    val id : String,
    val mention_everyone : Boolean = false,
    val mention_roles : List<String> = mutableListOf(),
    //val mentions : List<MessageMention> = mutableListOf(),
    val pinned : Boolean,
    val referenced_message : MessageCreateD? = null,
    val timestamp : String?,
    val tts : Boolean?,
    val type : Int,
    val guild_id : String? = null,
)

@Serializable
private data class User(
    val username : String = "",
    val discriminator : String = "",
    val avatar : String? = "",
    val id : String = "",
)

suspend fun messageCreate(payload : String, client : ClientImpl, eventBus : EventBus) {
    try {
        val d = json.decodeFromString<MessageCreate>(payload).d
        val event = ClientEvents.MessageCreate(
            Message(
                client.client,
                d.id,
                TextChannel(d.channel_id, client.client),
                client.utils.fetchGuild(d.guild_id ?: ""),
                org.caffeine.chaos.api.models.User(
                    d.author.username,
                    d.author.discriminator,
                    d.author.avatar,
                    d.author.id,
                ),
                null,
                d.content,
                tts = d.tts ?: false,
                mentionedEveryone = d.mention_everyone,
                nonce = d.id,
                pinned = d.pinned,
                type = d.type
            )
        )
        eventBus.produceEvent(event)
    }catch (e: Exception) {
        println("Error in messageCreate: ${e.printStackTrace()}")
    }
}
   