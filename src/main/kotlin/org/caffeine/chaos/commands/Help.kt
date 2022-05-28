package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.versionString

class Help : Command(arrayOf("help", "cmds", "commands")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**CHAOS v$versionString**")
            .appendLine("**Commands:** https://caffeine.moe/CHAOS/commands/")
            .build()
        ).thenAccept { message ->
            launch {
                onComplete(message, client, client.config.auto_delete.bot.content_generation)
            }
        }
    }
}