package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Restore : Command(arrayOf("restore")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {
        if (args.isEmpty()) {
            println("restoring from latest backup")
            return
        }
        return
    }
}