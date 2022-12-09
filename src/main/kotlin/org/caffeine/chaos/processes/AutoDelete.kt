package org.caffeine.chaos.processes

import kotlinx.coroutines.delay
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.config

suspend fun autoDeleteUser(event : ClientEvent.MessageCreate) {
    if (!config.auto_delete.user.enabled) return
    delay(config.auto_delete.user.delay * 1000)
    event.message.delete()
}

suspend fun autoDeleteBot(msg : Message) {
    if (!config.auto_delete.bot.enabled) return
    delay(config.auto_delete.bot.delay * 1000)
    msg.delete()
}
