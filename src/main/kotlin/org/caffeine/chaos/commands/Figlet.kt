package org.caffeine.chaos.commands

import com.github.lalyos.jfiglet.FigletFont
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Figlet : Command(arrayOf("fig", "figlet")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            if (args.isEmpty()) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("**Incorrect usage** '${event.message.content}'")
                    .appendLine("**Error:** No specified text to figletize")
                    .appendLine("**Correct usage:** `${client.config.prefix}figlet String`")
                    .build())
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            val textToFigletize = args.joinToString(" ")
            val figletizedText = FigletFont.convertOneLine(textToFigletize)
            event.channel.sendMessage(MessageBuilder()
                .appendLine("```$figletizedText```")
                .build())
                .thenAccept { message ->
                    this.launch { onComplete(message, client, client.config.auto_delete.bot.content_generation) }
                }
        }
}