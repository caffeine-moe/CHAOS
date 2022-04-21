package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config


suspend fun user(event: MessageCreateEvent, config: Config) {
    withContext(Dispatchers.IO) {
        Thread.sleep(config.auto_delete.user.delay * 1000)
    }
    event.message.delete(config)
}

suspend fun bot(msg: Message, config: Config) {
    if (config.auto_delete.bot.enabled) {
        withContext(Dispatchers.IO) {
            Thread.sleep(config.auto_delete.bot.delay * 1000)
        }
        try {
            msg.delete(config)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
