package org.caffeine.chaos.api.entities.channels

import arrow.core.Either
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.utils.MessageData

interface DMChannel : TextBasedChannel {
    val recipients : Map<String, User>
    override suspend fun sendMessage(data : MessageData) : CompletableDeferred<Either<String, Message>>
    override suspend fun sendMessage(text : String) : CompletableDeferred<Either<String, Message>>
    override suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message>
    override suspend fun fetchMessageById(id : String) : Message?
    override suspend fun delete()
}