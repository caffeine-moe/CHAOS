package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents


class Coin : Command(arrayOf("coin"), CommandInfo("Coin", "coin", "Flips a coin (Sends heads or tails).")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit =
        coroutineScope {
/*            val face = arrayOf("Heads", "Tails").random()
            event.channel.sendMessage(MessageBuilder()
                .appendLine(":coin: $face!").build()).thenAccept { message ->
                this.launch {
                    onComplete(message, client, client.config.auto_delete.bot.content_generation)
                }
            }*/
        }
}