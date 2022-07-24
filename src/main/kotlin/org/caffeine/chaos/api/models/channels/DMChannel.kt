package org.caffeine.chaos.api.models.channels

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.Message
import org.caffeine.chaos.api.models.User
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.utils.MessageFilters
import java.util.*
import java.util.concurrent.CompletableFuture

class DMChannel(
    override val id: String,
    override val client: Client,
    override val type: ChannelType,
    val lastMessageId: String,
    override val name: String,
    val recipients : Map<String, User>,
) : TextBasedChannel {
    override suspend fun sendMessage(payload : MessageOptions) : CompletableFuture<Message> {
       return client.user.sendMessage(this, payload)
    }
    override suspend fun messagesAsCollection(messageFilters : MessageFilters) : Collection<Message> {
        return client.utils.fetchMessagesAsCollection(this, messageFilters)
    }
    suspend fun delete() {
        client.user.deleteChannel(this)
    }
}