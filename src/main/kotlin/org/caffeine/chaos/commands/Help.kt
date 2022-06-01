package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.versionString

class Help : Command(arrayOf("help", "cmds", "commands"),
    CommandInfo("help [command]", "Sends the command list URL OR Displays info about a specified command.")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        //for me only lol
/*        val result = commandList.toSortedMap()
        for (command in result) {
            println("================================")
            println(command.toPair().first)
            if (command.toPair().second.commandNames.size > 1) {
                val sb = StringBuilder()
                for (i: String in command.toPair().second.commandNames) {
                    sb.append("$i ")
                }
                println("Aliases: $sb")
            }
            println("Usage: >" + command.toPair().second.commandInfo.usage)
            println("Description: " + command.toPair().second.commandInfo.description)
        }*/
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