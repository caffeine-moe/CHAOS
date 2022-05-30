package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.log

class CloseDm : Command(arrayOf("closedm")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {
        val message: String = when (event.channel.type()) {
            1, 3 -> {
                event.channel.delete()
                "closed!"
            }
            else -> {
                "is not a DM Channel!"
            }
        }
        log("Channel ${event.channel.id} $message", "RESPONSE:")
    }
}