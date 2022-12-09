package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent

class ReplyLatency : Command(arrayOf("rl"), CommandInfo("", "", "")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val m = event.channel.sendMessage("replied").await()
        m.also { println("${System.currentTimeMillis() - event.message.id.asUnixTs()}ms") }
    }
}