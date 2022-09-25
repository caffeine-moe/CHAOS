package org.caffeine.chaos.api.models.interfaces

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions

interface TextBasedChannel : BaseChannel {
    override val client : Client
    override val id : String
    override val name : String?
    override val type : ChannelType
    suspend fun sendMessage(payload : MessageOptions) : CompletableDeferred<Message>
    suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message>
    override suspend fun delete()
}