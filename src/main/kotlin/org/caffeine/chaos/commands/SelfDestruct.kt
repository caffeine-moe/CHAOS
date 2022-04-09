package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread
import kotlin.system.exitProcess

fun SelfDestruct(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.messageContent.lowercase() == "${config.prefix}quit" || event.messageContent.lowercase() == "${config.prefix}q" || event.messageContent.lowercase() == "${config.prefix}selfdestruct") {
            client.disconnect()
            exitProcess(69)
        }
    }
}