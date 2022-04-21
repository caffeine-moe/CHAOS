package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config
import kotlin.system.exitProcess

suspend fun selfDestruct(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content.lowercase() == "${config.prefix}quit" || event.message.content.lowercase() == "${config.prefix}q" || event.message.content.lowercase() == "${config.prefix}selfdestruct") {
        client.logout()
        exitProcess(69)
    }
}
