package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageCreateEvent


suspend fun user(event: MessageCreateEvent, client: Client) {
    if (client.config.auto_delete.user.enabled) {
        withContext(Dispatchers.IO) {
            Thread.sleep(client.config.auto_delete.user.delay * 1000)
        }
        event.message.delete(client)
    }
}

suspend fun bot(msg: Message, client: Client) {
    if (client.config.auto_delete.bot.enabled) {
        withContext(Dispatchers.IO) {
            Thread.sleep(client.config.auto_delete.bot.delay * 1000)
        }
        msg.delete(client)
    }
}
