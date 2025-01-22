package org.caffeine.chaos.commands

import org.caffeine.chaos.config
import org.caffeine.chaos.handlers.commandList
import org.caffeine.chaos.processes.autoDeleteBot
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.entities.message.Message
import org.caffeine.octane.typedefs.LogLevel
import org.caffeine.octane.typedefs.LoggerLevel
import org.caffeine.octane.utils.ConsoleColour
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.MessageSendData
import org.caffeine.octane.utils.log

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