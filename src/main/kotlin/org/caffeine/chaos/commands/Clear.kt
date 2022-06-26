package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.loginPrompt

class Clear : Command(arrayOf("clear"), CommandInfo("Clear", "clear", "Clears the CHAOS console.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {
        clear()
        loginPrompt(client)
    }
}