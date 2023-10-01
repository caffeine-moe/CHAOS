package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.ConsoleColour
import org.caffeine.octane.utils.awaitThen
import org.caffeine.octane.utils.log

class Token : Command(arrayOf("token"), CommandInfo("Token", "token", "Logs your token into the console.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        log("${client.user.username} : ${client.user.token}", "TOKEN:${ConsoleColour.BLUE.value}")
        event.message.channel.sendMessage("Token logged to console.")
            .awaitThen { message ->
                onComplete(message, true)
            }
    }
}