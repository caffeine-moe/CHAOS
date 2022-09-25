package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.log

class Token : Command(arrayOf("token"), CommandInfo("Token", "token", "Logs your token into the console.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit =
        coroutineScope {
            log("${client.user.discriminatedName} : ${client.user.token}", "TOKEN:${ConsoleColours.BLUE.value}")
            event.message.channel.sendMessage(MessageBuilder().append("Token logged to console.").build())
                .await().also { message ->
                    onComplete(message, client, true)
                }
        }
}