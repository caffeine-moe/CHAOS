package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.awaitThen

class Coin : Command(arrayOf("coin"), CommandInfo("Coin", "coin", "Flips a coin (Sends heads or tails).")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val face = arrayOf("Heads", "Tails").random()
        event.channel.sendMessage(":coin: $face!").awaitThen {
            onComplete(it, true)
        }
    }
}