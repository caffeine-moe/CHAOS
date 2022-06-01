package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

data class CommandInfo(
    val name: String,
    val usage: String,
    val description: String,
)

abstract class Command(val commandNames: Array<String>, val commandInfo: CommandInfo) {
    init {
        for (name in commandNames) {
            this.also { commandList[name] = it }
        }
    }

    open suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {}

    open suspend fun error(client: Client, event: MessageCreateEvent, error: String, info: CommandInfo): Message {
        return MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** $error")
            .appendLine("**Correct usage:** `${client.config.prefix}${info.usage}`")
            .build()
    }
}