package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.config

class Coin : Command(arrayOf("coin"), CommandInfo("Coin", "coin", "Flips a coin (Sends heads or tails).")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val face = arrayOf("Heads", "Tails").random()
        event.channel.sendMessage(":coin: $face!").await().also { message ->
            onComplete(message, config.auto_delete.bot.content_generation)
        }
    }
}
