package org.caffeine.chaos.commands

import org.caffeine.chaos.config
import org.caffeine.chaos.handlers.commandList
import org.caffeine.chaos.versionString
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen

class Help : Command(
    arrayOf("help", "cmds", "commands"),
    CommandInfo("Help", "help [command]", "Sends the command list URL OR Displays info about a specified command.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**CHAOS v$versionString**")
                    .appendLine("**Commands:** https://caffeine.moe/CHAOS/commands/")
            ).awaitThen { message ->
                onComplete(message, true)
            }
            return
        }
        if (args.first() == "all") {
            commandList.toList().sortedBy { it.first }.map { m ->
                val command = m.second
                "${command.commandInfo.name.trim()}${
                    if (command.commandNames.size > 1) "\nAliases: ${
                        command.commandNames.joinToString(
                            ", "
                        )
                    }" else ""
                }\nUsage: >${command.commandInfo.usage}\nDescription: ${command.commandInfo.description.trim()}"
            }.toSet().map { println("\n${it}") }
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
            event.channel.sendMessage(msg).awaitThen { message ->
                onComplete(message, true)
            }
            return
        }
        event.channel.sendMessage(error(client, event, "${args.joinToString(" ")} is not a command.", commandInfo))
            .awaitThen {
                onComplete(it, true)
            }
    }
}