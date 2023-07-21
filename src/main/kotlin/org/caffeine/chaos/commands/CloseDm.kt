package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.log

class CloseDm : Command(arrayOf("closedm"), CommandInfo("CloseDM", "closedm", "Closes a dm channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val message : String = when (event.channel.type) {
            ChannelType.DM,
            ChannelType.GROUP,
            -> {
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