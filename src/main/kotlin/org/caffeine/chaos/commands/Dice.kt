package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.awaitThen

class Dice :
    Command(arrayOf("dice", "d6"), CommandInfo("Dice", "dice", "Rolls a dice (Sends a random number from 1 - 6).")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        event.channel.sendMessage(
            (1..6).random().toString()
                .map { ":game_die: $it" }
                .first()
        ).awaitThen { message -> onComplete(message, true) }
    }
}