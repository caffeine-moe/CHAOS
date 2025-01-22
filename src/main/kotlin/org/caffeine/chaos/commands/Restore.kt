package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent

class Restore :
    Command(arrayOf("restore"), CommandInfo("Restore", "restore", "Restores your account from a backup json.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            println("restoring from latest backup")
            return
        }
        return
    }
}