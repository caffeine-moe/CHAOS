package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

abstract class Command(commandNames: Array<String>) {
    init {
        for (name in commandNames) {
            this.also { commandList[name] = it }
        }
    }

    open suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {}
}