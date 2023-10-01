package org.caffeine.chaos.commands

import org.caffeine.chaos.handlers.spamCock
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent

class SSpam : Command(arrayOf("sspam"), CommandInfo("SSpam", "sspam", "Stops the spammer.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        spamCock = true
    }
}