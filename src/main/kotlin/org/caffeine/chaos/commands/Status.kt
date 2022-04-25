package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientStatusType
import org.caffeine.chaos.api.client.message.MessageCreateEvent


suspend fun online(client: Client, event: MessageCreateEvent) {
    if (event.message.content == "${client.config.prefix}online") {
        client.setStatus(ClientStatusType.ONLINE)
    }
}

suspend fun away(client: Client, event: MessageCreateEvent) {
    if (event.message.content == "${client.config.prefix}idle" || event.message.content == "${client.config.prefix}away") {
        client.setStatus(ClientStatusType.IDLE)
    }
}

suspend fun dnd(client: Client, event: MessageCreateEvent) {
    if (event.message.content == "${client.config.prefix}dnd" || event.message.content == "${client.config.prefix}donotdisturb") {
        client.setStatus(ClientStatusType.DND)
    }
}

suspend fun invis(client: Client, event: MessageCreateEvent) {
    if (event.message.content == "${client.config.prefix}offline" || event.message.content == "${client.config.prefix}invis" || event.message.content == "${client.config.prefix}invisible") {
        client.setStatus(ClientStatusType.INVISIBLE)
    }
}

