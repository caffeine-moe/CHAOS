package org.caffeine.chaos.commands

import org.caffeine.chaos.processes.update
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent

class Update :
    Command(arrayOf("update", "updoot", "upgrade"), CommandInfo("update", "update", "Checks for CHAOS updates.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) = update()
}