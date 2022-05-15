package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.clear
import org.caffeine.chaos.loginPrompt

class Clear : Command(arrayOf("clear")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {
        clear()
        loginPrompt(client)
    }
}

