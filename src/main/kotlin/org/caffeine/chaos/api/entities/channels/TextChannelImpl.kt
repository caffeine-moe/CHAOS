package org.caffeine.chaos.api.entities.channels

import arrow.core.Either
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageBuilder
import org.caffeine.chaos.api.typedefs.MessageData

class TextChannelImpl(
    override var id : Snowflake,
    override var client : Client,
    override var type : ChannelType = ChannelType.TEXT,
    override var lastMessageId : Snowflake,
    override var name : String = "",
    override var position : Number = 0,
    override var parentId : Snowflake,
    override var topic : String = "",
    override var permissionOverwrites : Array<Any> = arrayOf(),
    override var nsfw : Boolean = false,
    override var rateLimitPerUser : Number = 0,
) : TextChannel {

    var guildId : Snowflake = Snowflake("")

    override var guild : Guild
        get() = client.user.guilds[guildId] ?: throw Exception("FUCK YOU")
        set(value) {
            guildId = value.id
        }

    override suspend fun sendMessage(data : MessageData) : CompletableDeferred<Either<String, Message>> {
        return client.user.sendMessage(this, data)
    }

    override suspend fun sendMessage(text : String) : CompletableDeferred<Either<String, Message>> {
        return client.user.sendMessage(this, MessageBuilder().append(text))
    }

    override suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message> {
        return client.user.fetchMessagesFromChannel(this, messageFilters)
    }

    override suspend fun fetchMessageById(id : String) : Message? {
        return client.user.fetchMessageById(id, this)
    }

    override suspend fun delete() {
        client.user.deleteChannel(this)
    }
}
