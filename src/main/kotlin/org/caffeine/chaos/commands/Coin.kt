package org.caffeine.chaos.commands

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

suspend fun Coin(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
        if (event.message.content.lowercase() == ("${config.prefix}coin")) {
            val face = arrayOf("heads", "tails").random()
            if (face == "heads") {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine(":coin: Heads!").build(), config, client).thenAccept { message -> this.launch{
                         bot(message, config)
                } }
                return@coroutineScope
            }
            else {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine(":coin: Tails!").build(), config, client).thenAccept { message -> this.launch{
                    bot(message, config)
                } }
                return@coroutineScope
            }
        }
}
