package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents


class Figlet :
    Command(arrayOf("figlet", "fig"), CommandInfo("Figlet", "fig <Text>", "Turns your text into an ascii figlet.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            if (args.isEmpty()) {
                event.channel.sendMessage(error(client, event, "No specified text to figletize.", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            try {
                val textToFigletize = args.joinToString(" ")
                val figletizedText = FigletFont.convertOneLine(textToFigletize)
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("```$figletizedText```")
                    .build())
                    .thenAccept { message ->
                        this.launch { onComplete(message, client, client.config.auto_delete.bot.content_generation) }
                    }
            } catch (e : ArrayIndexOutOfBoundsException) {
                event.channel.sendMessage(error(client, event, "Text contains non ASCII characters.", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }*/
        }
}