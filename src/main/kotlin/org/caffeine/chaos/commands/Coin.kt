package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config

suspend fun coin(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == ("${config.prefix}coin")) {
        val face = arrayOf("heads", "tails").random()
        if (face == "heads") {
            event.channel.sendMessage(MessageBuilder()
                .appendLine(":coin: Heads!").build(), config, client).thenAccept { message ->
                this.launch {
                    bot(message, config)
                }
            }
            return@coroutineScope
        } else {
            event.channel.sendMessage(MessageBuilder()
                .appendLine(":coin: Tails!").build(), config, client).thenAccept { message ->
                this.launch {
                    bot(message, config)
                }
            }
            return@coroutineScope
        }
    }
}
