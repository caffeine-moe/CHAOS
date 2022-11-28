package org.caffeine.chaos.api.entities.channels

import arrow.core.Either
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.utils.MessageData

interface TextBasedChannel : BaseChannel {
    val lastMessageId : Snowflake
    suspend fun sendMessage(data : MessageData) : CompletableDeferred<Either<String, Message>>
    suspend fun sendMessage(text : String) : CompletableDeferred<Either<String, Message>>
    suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message>

    suspend fun fetchMessageById(id : String) : Message?
    override suspend fun delete()
}
