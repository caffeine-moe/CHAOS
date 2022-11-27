package org.caffeine.chaos.api.entities.channels

import arrow.core.Either
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageData

interface TextChannel : GuildTextChannel {
    override val id : Snowflake
    override val client : Client
    override val type : ChannelType
    override val lastMessageId : Snowflake
    override val name : String
    override val position : Number
    override val parentId : Snowflake
    override val topic : String
    override val permissionOverwrites : Array<Any>
    override val nsfw : Boolean
    override val rateLimitPerUser : Number
    override val guild : Guild
    override suspend fun sendMessage(data : MessageData) : CompletableDeferred<Either<String, Message>>
    override suspend fun sendMessage(text : String) : CompletableDeferred<Either<String, Message>>
    override suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message>
    override suspend fun fetchMessageById(id : String) : Message?
    override suspend fun delete()

}