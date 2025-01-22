package org.caffeine.chaos.commands

import org.caffeine.chaos.utils.clear
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent

class Clear : Command(arrayOf("clear"), CommandInfo("Clear", "clear", "Clears the CHAOS console.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        clear()
    }
}