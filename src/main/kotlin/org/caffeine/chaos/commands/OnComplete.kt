package org.caffeine.chaos.commands

import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config
import org.caffeine.chaos.processes.autoDeleteBot

suspend fun onComplete(msg : Message, contentGeneration : Boolean) {
    if (config.logger.responses) {
        log(msg.content, "RESPONSE:${ConsoleColour.BLUE.value}")
    }
    if (!config.autoDelete.bot.enabled) return
    if (contentGeneration && !config.autoDelete.bot.contentGeneration) return
    autoDeleteBot(msg)
}