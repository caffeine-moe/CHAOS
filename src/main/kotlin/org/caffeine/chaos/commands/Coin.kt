package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun coin(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == ("${client.config.prefix}coin")) {
        val face = arrayOf("heads", "tails").random()
        if (face == "heads") {
            event.channel.sendMessage(MessageBuilder()
                .appendLine(":coin: Heads!").build(), client).thenAccept { message ->
                this.launch {
                    bot(message, client)
                }
            }
            return@coroutineScope
        } else {
            event.channel.sendMessage(MessageBuilder()
                .appendLine(":coin: Tails!").build(), client).thenAccept { message ->
                this.launch {
                    bot(message, client)
                }
            }
            return@coroutineScope
        }
    }
}
