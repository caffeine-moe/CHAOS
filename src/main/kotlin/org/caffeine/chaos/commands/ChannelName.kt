package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder

class ChannelName : Command(
    arrayOf("cn"),
    CommandInfo(
        "channel name",
        "cn",
        "cn"
    )
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        event.channel.sendMessage(
            MessageBuilder()
                .append(event.channel.name.toString())
                .build()
        )
    }
}
