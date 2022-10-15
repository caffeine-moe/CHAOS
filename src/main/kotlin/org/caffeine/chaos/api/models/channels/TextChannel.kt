package org.caffeine.chaos.api.models.channels

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.interfaces.GuildChannel
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions
import java.util.*

class TextChannel(
    override val id : String = "",
    override val client : Client,
    override val type : ChannelType = ChannelType.TEXT,
    override val lastMessageId : String = "",
    override val lastPinTimestamp : Date = Date(),
    override val name : String = "",
    override val position : Number = 0,
    override val parentId : String = "",
    override val topic : String = "",
    override val permissionOverwrites : Array<Any> = arrayOf(),
    override val nsfw : Boolean = false,
    override val rateLimitPerUser : Number = 0,
) : GuildChannel, TextBasedChannel {
    override suspend fun sendMessage(data : MessageOptions) : CompletableDeferred<Message> {
        return client.user.sendMessage(this, data)
    }

    override suspend fun sendMessage(text : String) : CompletableDeferred<Message> {
        return client.user.sendMessage(this, MessageOptions(text))
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
