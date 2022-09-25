package org.caffeine.chaos.api.models.channels

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions

class DMChannel(
    override val id : String,
    override val client : Client,
    override val type : ChannelType,
    val lastMessageId : String,
    override val name : String,
    val recipients : Map<String, User>,
) : TextBasedChannel {
    override suspend fun sendMessage(payload : MessageOptions) : CompletableDeferred<Message> {
        return client.user.sendMessage(this, payload)
    }

    override suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message> {
        return client.user.fetchMessagesFromChannel(this, messageFilters)
    }

    override suspend fun delete() {
        client.user.deleteChannel(this)
    }
}