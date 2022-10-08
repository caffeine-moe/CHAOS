package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.config

class Dice :
    Command(arrayOf("dice", "d6"), CommandInfo("Dice", "dice", "Rolls a dice (Sends a random number from 1 - 6).")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val num = (1..6).random()
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine(":game_die: $num").build()
        ).await().also { message ->
            onComplete(message, config.auto_delete.bot.content_generation)
        }
    }
}
