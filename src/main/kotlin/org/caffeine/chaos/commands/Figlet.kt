package org.caffeine.chaos.commands

import com.github.lalyos.jfiglet.FigletFont
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.config

class Figlet :
    Command(arrayOf("figlet", "fig"), CommandInfo("Figlet", "fig <Text>", "Turns your text into an ascii figlet.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            event.channel.sendMessage(error(client, event, "No specified text to figletize.", commandInfo))
                .await().also { message -> onComplete(message, true) }
            return
        }
        try {
            val textToFigletize = args.joinToString(" ")
            val figletizedText = FigletFont.convertOneLine(textToFigletize)
            event.channel.sendMessage("```$figletizedText```").await().also { message ->
                onComplete(message, config.auto_delete.bot.content_generation)
            }
        } catch (e : ArrayIndexOutOfBoundsException) {
            event.channel.sendMessage(error(client, event, "Text contains non ASCII characters.", commandInfo))
                .await().also { message -> onComplete(message, true) }
            return
        }
    }
}
