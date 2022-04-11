package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Config
import org.caffeine.chaos.LogV2
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun Token(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == "${config.prefix}token") {
        LogV2(config.token, "TOKEN:\u001B[38;5;33m")
        try {
            event.message.channel.sendMessage(MessageBuilder().append("Token logged to console.").build(),
                config,
                client)
                .thenAccept { message ->
                    this.launch { bot(message, config) }
                }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}