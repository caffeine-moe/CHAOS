package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientGuild
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun msar(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}msar") {
        val guilds = client.user.guilds.getList()
        for (guild: ClientGuild in guilds) {
            println("lol")
        }
    }
}