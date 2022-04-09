package org.caffeine.chaos.commands

import io.ktor.client.features.websocket.*
import org.caffeine.chaos.Config
import org.caffeine.chaos.LogV2
import org.caffeine.chaos.api.Client
import org.caffeine.chaos.api.messageCreate
import org.caffeine.chaos.api.sendMessage
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

suspend fun Token(client: Client, event: messageCreate, config: Config) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.d.content.lowercase() == "${config.prefix}token") {
            LogV2(config.token, "TOKEN:\u001B[38;5;33m")
            sendMessage(event.d.channel_id,"Token logged to console.", config)
        }
}