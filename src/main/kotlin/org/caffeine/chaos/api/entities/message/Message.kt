package org.caffeine.chaos.api.entities.message

import arrow.core.Either
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.api.utils.MessageData
import java.util.*

interface Message : MessageData {
    val client : Client
    val id : Snowflake
    val channel : TextBasedChannel
    val guild : Guild?
    val author : User
    val timestamp : Date
    val editedAt : Date?
    val mentionedEveryone : Boolean
    val mentions : Map<String, User>
    val attachments : Map<String, MessageAttachment>
    val pinned : Boolean
    val type : MessageType

    suspend fun delete()

    suspend fun edit(edit : MessageData) : CompletableDeferred<Either<String, Message>>

    suspend fun edit(text : String) : CompletableDeferred<Either<String, Message>>

    suspend fun reply(text : MessageData) : CompletableDeferred<Either<String, Message>>

    suspend fun reply(text : String) :  CompletableDeferred<Either<String, Message>>
}