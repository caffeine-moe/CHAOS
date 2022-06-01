package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commandList
import org.caffeine.chaos.versionString

class Help : Command(arrayOf("help", "cmds", "commands"),
    CommandInfo("Help","help [command]", "Sends the command list URL OR Displays info about a specified command.")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        if (args.isEmpty()) {
            event.channel.sendMessage(MessageBuilder()
                .appendLine("**CHAOS v$versionString**")
                .appendLine("**Commands:** https://caffeine.moe/CHAOS/commands/")
                .build()
            ).thenAccept { message ->
                launch {
                    onComplete(message, client, client.config.auto_delete.bot.content_generation)
                }
            }
            return@coroutineScope
        }
        val command: Command? = commandList[args.first().replace(client.config.prefix, "")]
        if (command != null) {
            val msg = MessageBuilder()
                .appendLine("**${command.commandInfo.name}**")
            val sb = StringBuilder()
            if (command.commandNames.size > 1) {
                for (i: String in command.commandNames) {
                    sb.append("$i ")
                }
                if (sb.isNotBlank()) {
                    msg.appendLine("**Aliases:** $sb")
                }
            }
            msg.appendLine("**Usage:** ${client.config.prefix}${command.commandInfo.usage}")
            msg.appendLine("**Description:** ${command.commandInfo.description}")
            event.channel.sendMessage(msg.build()).thenAccept { message ->
                launch {
                    onComplete(message, client, client.config.auto_delete.bot.content_generation)
                }
            }
            return@coroutineScope
        }
        event.channel.sendMessage(error(client, event, "${args.joinToString(" ")} is not a command.", commandInfo)).thenAccept {
            launch { onComplete(it, client, true) }
        }
    }
}