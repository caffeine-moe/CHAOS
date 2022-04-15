package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.loginPrompt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun Clear(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content.lowercase() == ("${config.prefix}clear")) {
        print("\u001b[H\u001b[2J\u001B[38;5;255m")
        loginPrompt(client, config)
    }
}

