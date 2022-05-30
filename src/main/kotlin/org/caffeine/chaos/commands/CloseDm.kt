package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.log

class CloseDm : Command(arrayOf("closedm")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {
        when (event.channel.type()) {
            1, 3 -> {
                event.channel.delete()
                log("Channel ${event.channel.id} closed!", "RESPONSE:")
                return
            }
        }
        log("Channel ${event.channel.id} is not a DM Channel!", "RESPONSE:")
    }
}