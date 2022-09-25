package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.*
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder


class Help : Command(
    arrayOf("help", "cmds", "commands"),
    CommandInfo("Help", "help [command]", "Sends the command list URL OR Displays info about a specified command.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
        if (args.isEmpty()) {
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**CHAOS v$versionString**")
                    .appendLine("**Commands:** https://caffeine.moe/CHAOS/commands/")
                    .build()
            ).await().also { message ->
                onComplete(message, client, config.auto_delete.bot.content_generation)
            }
            return@coroutineScope
        }
        val command : Command? = commandList[args.first().replace(config.prefix, "")]
        if (command != null) {
            val msg = MessageBuilder()
                .appendLine("__**${command.commandInfo.name}**__")
            val sb = StringBuilder()
            if (command.commandNames.size > 1) {
                for (i : String in command.commandNames) {
                    sb.append("$i ")
                }
                if (sb.isNotBlank()) {
                    msg.appendLine("**Aliases:** $sb")
                }
            }
            msg.appendLine("**Usage:** ${config.prefix}${command.commandInfo.usage}")
            msg.appendLine("**Description:** ${command.commandInfo.description}")
            event.channel.sendMessage(msg.build()).await().also { message ->
                launch {
                    onComplete(message, client, config.auto_delete.bot.content_generation)
                }
            }
            return@coroutineScope
        }
        event.channel.sendMessage(error(client, event, "${args.joinToString(" ")} is not a command.", commandInfo))
            .await().also {
                launch { onComplete(it, client, true) }
            }
    }
}