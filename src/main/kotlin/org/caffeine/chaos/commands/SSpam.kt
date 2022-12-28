package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.handlers.spamCock

class SSpam : Command(arrayOf("sspam"), CommandInfo("SSpam", "sspam", "Stops the spammer.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) = run { spamCock = true }
}