package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.awaitThen

class ReplyLatency : Command(arrayOf("rl"), CommandInfo("", "", "")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        event.channel.sendMessage("replied").awaitThen {
            println("${System.currentTimeMillis() - event.message.timestamp}ms")
        }
    }
}