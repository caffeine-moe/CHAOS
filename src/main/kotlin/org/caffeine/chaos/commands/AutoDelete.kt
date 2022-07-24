package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.Message
import org.caffeine.chaos.config


suspend fun user(event : ClientEvents.MessageCreate) {
    if (config.auto_delete.user.enabled) {
        withContext(Dispatchers.IO) {
            Thread.sleep(config.auto_delete.user.delay * 1000)
        }
        event.message.delete()
    }
}

suspend fun bot(msg : Message) {
    if (config.auto_delete.bot.enabled) {
        withContext(Dispatchers.IO) {
            Thread.sleep(config.auto_delete.bot.delay * 1000)
        }
        msg.delete()
    }
}
