package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent

class Restore :
    Command(arrayOf("restore"), CommandInfo("Restore", "restore", "Restores your account from a backup json.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            println("restoring from latest backup")
            return
        }
        return
    }
}
