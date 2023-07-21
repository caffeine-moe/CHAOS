package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.MessageUpdate
import org.caffeine.chaos.api.entities.asSnowflake
import org.caffeine.chaos.api.entities.message.MessageImpl
import org.caffeine.chaos.api.utils.json

suspend fun messageUpdate(payload : String, client : ClientImpl) {
    val d = json.decodeFromString<MessageUpdate>(payload).d
    val old = client.user.messageCache[d.id.asSnowflake()] ?: return
    old as MessageImpl
    val new = old.copy(
        content = d.content ?: old.content,
        editedAt = client.utils.timestampResolver(d.edited_timestamp)
    )
    client.userImpl.messageCache[old.id] = new
    val event = ClientEvent.MessageEdit(
        new
    )
    client.eventBus.produceEvent(event)
}