package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.payloads.gateway.MessageCreate

suspend fun messageCreate(payload : String, client : ClientImpl) {
    try {
        val d = json.decodeFromString<MessageCreate>(payload).d
        val message = client.utils.createMessage(d)
        val channel = message.channel
        val event = ClientEvents.MessageCreate(
            message,
            channel
        )
        client.eventBus.produceEvent(event)
    } catch (e : Exception) {
        println("Error in messageCreate: ${e.message}")
        e.printStackTrace()
    }
}
