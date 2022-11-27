package org.caffeine.chaos.commands

import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.autoDeleteBot
import org.caffeine.chaos.config

suspend fun onComplete(msg : Message, autoDelete : Boolean) {
    if (config.logger.responses) {
        val contentInline = msg.content.replace("\n", "\n                                  ")
        log(contentInline, "RESPONSE:${ConsoleColour.BLUE.value}")
    }
    if (autoDelete && config.auto_delete.bot.enabled) {
        autoDeleteBot(msg)
    }
}
