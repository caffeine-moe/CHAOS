package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class TestCommand : Command(arrayOf("testnew", "tn")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>) {
        event.channel.sendMessage(MessageBuilder().appendLine("Test works!!").build())
    }
}