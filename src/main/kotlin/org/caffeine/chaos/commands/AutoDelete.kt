package org.caffeine.chaos.commands

import kotlinx.coroutines.delay
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.config

suspend fun user(event : ClientEvents.MessageCreate) {
    if (!config.auto_delete.user.enabled) return
    delay(config.auto_delete.user.delay * 1000)
    event.message.delete()
}

suspend fun bot(msg : Message) {
    if (!config.auto_delete.bot.enabled) return
    delay(config.auto_delete.bot.delay * 1000)
    msg.delete()
}
