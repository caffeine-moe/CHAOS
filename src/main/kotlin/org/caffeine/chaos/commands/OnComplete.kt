package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.log

suspend fun onComplete(msg : Message, client : Client, autoDelete : Boolean) = coroutineScope {
    if (client.config.logger.responses) {
        val ind = msg.content.replace("\n", "\n                              ")
        log(ind, "RESPONSE:\u001B[38;5;33m")
    }
    if (autoDelete && client.config.auto_delete.bot.enabled) {
        this.launch { bot(msg, client) }
    }
}