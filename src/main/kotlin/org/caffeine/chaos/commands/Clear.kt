package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.loginPrompt

suspend fun clear(client: Client, event: MessageCreateEvent) {
    if (event.message.content.lowercase() == ("${client.config.prefix}clear")) {
        print("\u001b[H\u001b[2J\u001B[38;5;255m")
        loginPrompt(client)
    }
}

