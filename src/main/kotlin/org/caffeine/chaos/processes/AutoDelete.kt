package org.caffeine.chaos.processes

import kotlinx.coroutines.delay
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.config

suspend fun autoDeleteUser(event : ClientEvent.MessageCreate) {
    if (!config.autoDelete.user.enabled) return
    delay(config.autoDelete.user.delay * 1000)
    event.message.delete()
}

suspend fun autoDeleteBot(msg : Message) {
    if (!config.autoDelete.bot.enabled) return
    delay(config.autoDelete.bot.delay * 1000)
    msg.delete()
}