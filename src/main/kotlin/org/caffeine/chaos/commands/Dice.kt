package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.awaitThen

class Dice :
    Command(arrayOf("dice", "d6"), CommandInfo("Dice", "dice", "Rolls a dice (Sends a random number from 1 - 6).")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        (1..6).random().toString()
            .map { ":game_die: $it" }
            .first()
            .also { event.channel.sendMessage(it).awaitThen { message -> onComplete(message, true) } }
    }
}