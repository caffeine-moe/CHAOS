package org.caffeine.chaos.commands

import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config

suspend fun onComplete(msg : Message, autoDelete : Boolean) {
    if (config.logger.responses) {
        val contentInline = msg.content.replace("\n", "\n                                  ")
        log(contentInline, "RESPONSE:${ConsoleColours.BLUE.value}")
    }
    if (autoDelete && config.auto_delete.bot.enabled) {
        bot(msg)
    }
}
