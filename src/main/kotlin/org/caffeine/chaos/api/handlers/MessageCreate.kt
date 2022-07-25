package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.EventBus
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.payloads.gateway.MessageCreate

suspend fun messageCreate(payload : String, client : ClientImpl) {
    try {
        val d = jsonc.decodeFromString<MessageCreate>(payload).d
        val event = ClientEvents.MessageCreate(client.utils.createMessage(d))
        client.eventBus.produceEvent(event)
    } catch (e : Exception) {
        println("Error in messageCreate: ${e.message}")
        e.printStackTrace()
    }
}
   