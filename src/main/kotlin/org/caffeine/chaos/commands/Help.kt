package org.caffeine.chaos.commands

import org.caffeine.chaos.*
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder

class Help : Command(
    arrayOf("help", "cmds", "commands"),
    CommandInfo("Help", "help [command]", "Sends the command list URL OR Displays info about a specified command.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**CHAOS v$versionString**")
                    .appendLine("**Commands:** https://caffeine.moe/CHAOS/commands/")
            ).await().map { message ->
                onComplete(message, config.auto_delete.bot.content_generation)
            }
            return
        }
        val command : Command? = commandList[args.first().replace(config.prefix, "")]
        if (command != null) {
            val msg = MessageBuilder()
                .append("```asciidoc")
                .appendLine(command.commandInfo.name)
                .appendLine("".padStart(command.commandInfo.name.length, '-'))
            if (command.commandNames.size > 1) {
                msg.appendLine("- Aliases: ${command.commandNames.joinToString(", ")}")
            }
            msg.appendLine("- Usage: ${config.prefix}${command.commandInfo.usage}")
            msg.appendLine("- Description: ${command.commandInfo.description}")
            msg.appendLine("```")
            event.channel.sendMessage(msg).await().map { message ->
                onComplete(message, config.auto_delete.bot.content_generation)
            }
            return
        }
        event.channel.sendMessage(error(client, event, "${args.joinToString(" ")} is not a command.", commandInfo))
            .await().map {
                onComplete(it, true)
            }
    }
}
