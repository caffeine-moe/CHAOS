package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.log

suspend fun token(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}token") {
        log(client.config.token, "TOKEN:\u001B[38;5;33m")
        try {
            event.channel.sendMessage(MessageBuilder().append("Token logged to console.").build(),
                client)
                .thenAccept { message ->
                    this.launch { bot(message, client) }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}