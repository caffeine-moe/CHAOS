package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.message.MessageCreateEvent
import org.caffeine.chaos.update

class Update :
    Command(arrayOf("update", "updoot", "upgrade"), CommandInfo("update", "update", "Checks for CHAOS updates.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {
        update(client)
    }
}