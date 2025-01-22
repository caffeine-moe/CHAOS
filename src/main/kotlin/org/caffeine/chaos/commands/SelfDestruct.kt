package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import kotlin.system.exitProcess

class SelfDestruct : Command(arrayOf("quit", "q", "selfdestruct"), CommandInfo("Quit", "quit", "Exits CHAOS.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        client.logout()
        exitProcess(69)
    }
}