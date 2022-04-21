package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientStatusType
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config


suspend fun online(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content == "${config.prefix}online") {
        client.setStatus(config, ClientStatusType.ONLINE)
    }
}

suspend fun away(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content == "${config.prefix}idle" || event.message.content == "${config.prefix}away") {
        client.setStatus(config, ClientStatusType.IDLE)
    }
}

suspend fun dnd(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content == "${config.prefix}dnd" || event.message.content == "${config.prefix}donotdisturb") {
        client.setStatus(config, ClientStatusType.DND)
    }
}

suspend fun invis(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content == "${config.prefix}offline" || event.message.content == "${config.prefix}invis" || event.message.content == "${config.prefix}invisible") {
        client.setStatus(config, ClientStatusType.INVISIBLE)
    }
}

