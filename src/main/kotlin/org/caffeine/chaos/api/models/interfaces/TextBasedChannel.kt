package org.caffeine.chaos.api.models.interfaces

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.Message
import org.caffeine.chaos.api.models.channels.BaseChannel
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.utils.MessageFilters
import java.util.concurrent.CompletableFuture

interface TextBasedChannel : BaseChannel {
    override val client : Client
    override val id : String
    override val name : String?
    override val type : ChannelType
    suspend fun sendMessage(payload : MessageOptions) : CompletableFuture<Message>
    suspend fun messagesAsCollection(messageFilters : MessageFilters) : Collection<Message>
}