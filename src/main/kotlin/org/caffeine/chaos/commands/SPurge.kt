package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents

import org.caffeine.chaos.purgeCock

class SPurge : Command(arrayOf("spurge"), CommandInfo("SPurge", "spurge", "Stops the purger.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        purgeCock = true
    }
}