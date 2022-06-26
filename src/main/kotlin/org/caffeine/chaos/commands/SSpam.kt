package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.message.MessageCreateEvent
import org.caffeine.chaos.spamCock

class SSpam : Command(arrayOf("sspam"), CommandInfo("SSpam", "sspam", "Stops the spammer.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {
        spamCock = true
    }
}