package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Dice :
    Command(arrayOf("dice", "d6"), CommandInfo("Dice", "dice", "Rolls a dice (Sends a random number from 1 - 6).")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) : Unit =
        coroutineScope {
/*            val num = (1..6).random()
            event.channel.sendMessage(MessageBuilder()
                .appendLine(":game_die: $num").build()).thenAccept { message ->
                this.launch {
                    onComplete(message, client, client.config.auto_delete.bot.content_generation)
                }
            }*/
        }
}