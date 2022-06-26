package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.message.MessageCreateEvent
import kotlin.system.exitProcess

class SelfDestruct : Command(arrayOf("quit", "q", "selfdestruct"), CommandInfo("Quit", "quit", "Exits CHAOS.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {
        client.logout()
        exitProcess(69)
    }
}