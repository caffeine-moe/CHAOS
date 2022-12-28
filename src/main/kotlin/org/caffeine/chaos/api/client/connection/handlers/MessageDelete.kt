package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.MessageDelete
import org.caffeine.chaos.api.entities.asSnowflake
import org.caffeine.chaos.api.utils.json

suspend fun messageDelete(payload : String, client : ClientImpl) {
    val d = json.decodeFromString<MessageDelete>(payload).d
    val event = ClientEvent.MessageDelete(
        client.user.messageCache[d.id.asSnowflake()] ?: return
    )
    client.eventBus.produceEvent(event)
}