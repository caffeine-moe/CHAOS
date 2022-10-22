package org.caffeine.chaos.api.client.connection.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.MessageCreate
import org.caffeine.chaos.api.json

suspend fun messageCreate(payload : String, client : ClientImpl) {
    try {
        val d = json.decodeFromString<MessageCreate>(payload).d
        val message = client.utils.createMessage(d)
        val channel = message.channel
        val event = ClientEvent.MessageCreate(
            message,
            channel
        )
        client.eventBus.produceEvent(event)
    } catch (e : Exception) {
        println("Error in messageCreate: ${e.message}")
        e.printStackTrace()
    }
}
