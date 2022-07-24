package org.caffeine.chaos.api.handlers

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.EventBus
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.models.Message
import org.caffeine.chaos.api.models.channels.TextChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.payloads.gateway.MessageCreate

suspend fun messageCreate(payload : String, client : ClientImpl, eventBus : EventBus) {
    try {
        val d = jsonc.decodeFromString<MessageCreate>(payload).d
        val event = ClientEvents.MessageCreate(client.utils.createMessage(d))
        eventBus.produceEvent(event)
    }catch (e: Exception) {
        println("Error in messageCreate: ${e.message}")
        e.printStackTrace()
    }
}
   