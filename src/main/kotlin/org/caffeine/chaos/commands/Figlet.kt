package org.caffeine.chaos.commands

import com.github.lalyos.jfiglet.FigletFont
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.awaitThen

class Figlet :
    Command(arrayOf("figlet", "fig"), CommandInfo("Figlet", "fig <Text>", "Turns your text into an ascii figlet.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            event.channel.sendMessage(error(client, event, "No specified text to figletize.", commandInfo))
                .awaitThen {
                    onComplete(it, false)
                }
            return
        }
        try {
            val textToFigletize = args.joinToString()
            val figletizedText = FigletFont.convertOneLine(textToFigletize)
            event.channel.sendMessage("```\n$figletizedText```").awaitThen {
                onComplete(it, true)
            }
        } catch (e : ArrayIndexOutOfBoundsException) {
            event.channel.sendMessage(error(client, event, "Text contains non ASCII characters.", commandInfo))
                .awaitThen {
                    onComplete(it, false)
                }
            return
        }
    }
}