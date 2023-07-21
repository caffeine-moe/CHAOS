package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.MessageCreate
import org.caffeine.chaos.api.utils.json

suspend fun messageCreate(payload : String, client : ClientImpl) {
    val d = json.decodeFromString<MessageCreate>(payload).d
    val result = client.utils.createMessage(d)
    client.userImpl.messageCache[result.id] = result
    val event = ClientEvent.MessageCreate(
        result,
        result.channel
    )
    client.eventBus.produceEvent(event)
}