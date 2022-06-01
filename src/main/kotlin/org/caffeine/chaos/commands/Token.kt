package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.log

class Token : Command(arrayOf("token"), CommandInfo("Token","token", "Logs your token into the console.")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            if (event.message.content.lowercase() == "${client.config.prefix}token") {
                log(client.config.token, "TOKEN:\u001B[38;5;33m")
                try {
                    event.channel.sendMessage(MessageBuilder().append("Token logged to console.").build())
                        .thenAccept { message ->
                            this.launch { onComplete(message, client, true) }
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
}