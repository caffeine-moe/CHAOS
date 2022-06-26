package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.message.Message
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log

suspend fun onComplete(msg : Message, client : Client, autoDelete : Boolean) = coroutineScope {
    if (client.config.logger.responses) {
        val ind = msg.content.replace("\n", "\n                              ")
        log(ind, "RESPONSE:${ConsoleColours.BLUE.value}")
    }
    if (autoDelete && client.config.auto_delete.bot.enabled) {
        this.launch { bot(msg, client) }
    }
}