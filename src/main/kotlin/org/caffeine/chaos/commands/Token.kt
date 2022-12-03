package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log

class Token : Command(arrayOf("token"), CommandInfo("Token", "token", "Logs your token into the console.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        log("${client.user.discriminatedName} : ${client.user.token}", "TOKEN:${ConsoleColour.BLUE.value}")
        event.message.channel.sendMessage(MessageBuilder().append("Token logged to console."))
            .await().also { message ->
                onComplete(message, true)
            }
    }
}
