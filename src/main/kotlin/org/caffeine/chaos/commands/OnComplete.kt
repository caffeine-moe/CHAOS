package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message

suspend fun onComplete(msg : Message, client : Client, autoDelete : Boolean) = coroutineScope {
/*    if (client.config.logger.responses) {
        val ind = msg.content.replace("\n", "\n                              ")
        log(ind, "RESPONSE:${ConsoleColours.BLUE.value}")
    }
    if (autoDelete && client.config.auto_delete.bot.enabled) {
        this.launch { bot(msg, client) }
    }*/
}