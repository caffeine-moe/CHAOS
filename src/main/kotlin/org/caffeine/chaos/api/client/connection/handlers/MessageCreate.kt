package org.caffeine.chaos.api.client.connection.handlers

import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.left
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.MessageCreate
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.log

suspend fun messageCreate(payload : String, client : ClientImpl) {
    val d = json.decodeFromString<MessageCreate>(payload).d
    when (val result = client.utils.createMessage(d)) {
        is Invalid -> {
            log("Error in messageCreate: ${result.left().map { it }}", level = LogLevel(LoggerLevel.LOW, client))
            return
        }

        is Valid -> {
            val event = ClientEvent.MessageCreate(
                result.value,
                result.value.channel
            )
            client.eventBus.produceEvent(event)
        }
    }
}
