package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log

class Token : Command(arrayOf("token"), CommandInfo("Token", "token", "Logs your token into the console.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
                log(client.utils.token, "TOKEN:${ConsoleColours.BLUE.value}")
/*                event.channel.sendMessage(MessageBuilder().append("Token logged to console.").build())
                    .thenAccept { message ->
                        this.launch { onComplete(message, client, true) }
                    }*/
        }
}