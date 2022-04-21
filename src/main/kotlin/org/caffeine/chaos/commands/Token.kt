package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.log

suspend fun token(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == "${config.prefix}token") {
        log(config.token, "TOKEN:\u001B[38;5;33m")
        try {
            event.channel.sendMessage(MessageBuilder().append("Token logged to console.").build(),
                config,
                client)
                .thenAccept { message ->
                    this.launch { bot(message, config) }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}