package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.log

suspend fun closeDM(client: Client, event: MessageCreateEvent) {
    if (event.message.content == "${client.config.prefix}closedm") {
        if (event.channel.type() == 1) {
            event.channel.delete()
            log("Channel ${event.channel.id} closed!", "RESPONSE:")
            return
        }
        log("Channel ${event.channel.id} is not a DM Channel!", "RESPONSE:")
    }
}