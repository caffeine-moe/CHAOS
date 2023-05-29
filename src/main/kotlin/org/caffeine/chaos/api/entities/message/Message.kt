package org.caffeine.chaos.api.entities.message

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.guild.GuildMember
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.api.utils.MessageData

interface Message : MessageData {
    val client : Client
    val id : Snowflake
    val channel : TextBasedChannel
    val guild : Guild?
    val guildMember : GuildMember?
    val author : User
    val timestamp : Long
    val editedAt : Long?
    val mentionedEveryone : Boolean
    val mentions : Map<String, User>
    val pinned : Boolean
    val type : MessageType

    suspend fun delete()

    suspend fun edit(edit : MessageData) : CompletableDeferred<Message>

    suspend fun edit(text : String) : CompletableDeferred<Message>

    suspend fun reply(text : MessageData) : CompletableDeferred<Message>

    suspend fun reply(text : String) : CompletableDeferred<Message>
}