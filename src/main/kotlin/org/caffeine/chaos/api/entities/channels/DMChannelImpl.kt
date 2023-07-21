package org.caffeine.chaos.api.entities.channels

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageSendData

class DMChannelImpl(
    override val id : Snowflake,
    override val client : Client,
    override val type : ChannelType,
    override val lastMessageId : Snowflake,
    override val name : String,
    override val recipients : Map<Snowflake, User>,
) : DMChannel {
    override suspend fun sendMessage(data : MessageSendData) : CompletableDeferred<Message> {
        return client.user.sendMessage(this, data)
    }

    override suspend fun sendMessage(text : String) : CompletableDeferred<Message> {
        return client.user.sendMessage(this, MessageBuilder().append(text))
    }

    override suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message> {
        return client.user.fetchMessagesFromChannel(this, messageFilters)
    }

    override suspend fun fetchMessageById(id : String) : Message {
        return client.user.fetchMessageById(id, this)
    }

    override suspend fun delete() {
        client.user.deleteChannel(this)
    }
}