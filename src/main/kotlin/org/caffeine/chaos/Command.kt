package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.log

abstract class Command(val commandNames : Array<String>, val commandInfo : CommandInfo) {
    init {
        for (name in commandNames) {
            this.also { commandList[name] = it }
        }
    }

    open suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        log("Not implemented.")
    }

    open suspend fun error(
        client : Client,
        event : ClientEvents.MessageCreate,
        error : String,
        info : CommandInfo,
    ) : MessageOptions {
        return MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** $error")
            .appendLine("**Correct usage:** `${config.prefix}${info.usage}`")
            .build()
    }
}