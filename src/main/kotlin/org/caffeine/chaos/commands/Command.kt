package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageSendData
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config
import org.caffeine.chaos.handlers.commandList
import org.caffeine.chaos.processes.autoDeleteBot

sealed class Command(val commandNames : Array<String>, val commandInfo : CommandInfo) {

    init {
        commandList.putAll(commandNames.associateBy({ it }, { this }))
    }

    open suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        log("Not implemented.", level = LogLevel(LoggerLevel.LOW, client))
    }

    open suspend fun error(
        client : Client,
        event : ClientEvent.MessageCreate,
        error : String,
        info : CommandInfo,
    ) : MessageSendData {
        return MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** $error")
            .appendLine("**Correct usage:** `${config.prefix}${info.usage}`")
    }

    open suspend fun onComplete(msg : Message, contentGeneration : Boolean) {
        if (config.logger.responses) {
            log(msg.content, "RESPONSE:${ConsoleColour.BLUE.value}")
        }
        if (!config.autoDelete.bot.enabled) return
        if (contentGeneration && !config.autoDelete.bot.contentGeneration) return
        autoDeleteBot(msg)
    }

    open suspend fun onComplete(msg : String) {
        if (config.logger.responses) {
            log(msg, "RESPONSE:${ConsoleColour.BLUE.value}")
        }
    }
}