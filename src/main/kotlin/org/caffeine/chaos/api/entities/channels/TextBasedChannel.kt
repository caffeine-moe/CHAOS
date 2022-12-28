package org.caffeine.chaos.api.entities.channels

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.utils.MessageData

interface TextBasedChannel : BaseChannel {
    val lastMessageId : Snowflake
    suspend fun sendMessage(data : MessageData) : CompletableDeferred<Message>
    suspend fun sendMessage(text : String) : CompletableDeferred<Message>
    suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message>

    suspend fun fetchMessageById(id : String) : Message?
}
