package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.client.DiscordChannelType
import org.caffeine.chaos.api.discord.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.log

class CloseDm : Command(arrayOf("closedm"), CommandInfo("CloseDM", "closedm", "Closes a dm channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {
        val message : String = when (event.channel.type()) {
            DiscordChannelType.DM,
            DiscordChannelType.GROUP,
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