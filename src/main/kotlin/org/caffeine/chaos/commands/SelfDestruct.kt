package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import kotlin.system.exitProcess

suspend fun selfDestruct(client: Client, event: MessageCreateEvent) {
    if (event.message.content.lowercase() == "${client.config.prefix}quit" || event.message.content.lowercase() == "${client.config.prefix}q" || event.message.content.lowercase() == "${client.config.prefix}selfdestruct") {
        client.logout()
        exitProcess(69)
    }
}
