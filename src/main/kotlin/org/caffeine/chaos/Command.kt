package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

abstract class Command(commandNames: Array<String>) {
    init {
        for (name in commandNames) {
            commandList2[name] = this
        }
    }

    open suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>) {}
}