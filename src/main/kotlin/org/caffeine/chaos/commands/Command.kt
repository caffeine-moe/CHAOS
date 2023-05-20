package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageData
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config
import org.caffeine.chaos.handlers.commandList

sealed class Command(val commandNames : Array<String>, val commandInfo : CommandInfo) {
    init {
        commandList.putAll(commandNames.associateBy({ it }, { this }))
    }

    open suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        log("Not implemented.", level = LogLevel(LoggerLevel.LOW, client))
    }

    open suspend fun error(
        client : Client,
        event : ClientEvent.MessageCreate,
        error : String,
        info : CommandInfo,
    ) : MessageData {
        return MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** $error")
            .appendLine("**Correct usage:** `${config.prefix}${info.usage}`")
    }
}