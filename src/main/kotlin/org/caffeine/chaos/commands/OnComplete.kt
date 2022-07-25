package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config

suspend fun onComplete(msg : Message, client : Client, autoDelete : Boolean) = coroutineScope {
    if (config.logger.responses) {
        val contentInline = msg.content.replace("\n", "\n                                  ")
        log(contentInline, "RESPONSE:${ConsoleColours.BLUE.value}")
    }
    if (autoDelete && config.auto_delete.bot.enabled) {
        this.launch { bot(msg) }
    }
}