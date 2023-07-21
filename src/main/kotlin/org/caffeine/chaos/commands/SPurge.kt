package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.handlers.purgeCock

class SPurge : Command(arrayOf("spurge"), CommandInfo("SPurge", "spurge", "Stops the purger.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        purgeCock = true
    }
}