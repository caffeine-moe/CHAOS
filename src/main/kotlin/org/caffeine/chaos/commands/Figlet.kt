package org.caffeine.chaos.commands

import com.github.lalyos.jfiglet.FigletFont
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun figlet(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content == "${client.config.prefix}figlet"
    ) {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** No specified text to figletize")
            .appendLine("**Correct usage:** `${client.config.prefix}figlet String`")
            .build(), client)
            .thenAccept { message -> this.launch { bot(message, client) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}figlet ") && event.message.content != "${client.config.prefix}figlet "
    ) {
        val texttofigletize =
            event.message.content.removePrefix("${client.config.prefix}figlet ").trimIndent().replace("\n", " ")
        val figletizedtext = FigletFont.convertOneLine(texttofigletize)
        event.channel.sendMessage(MessageBuilder()
            .appendLine("```$figletizedtext```")
            .build(), client)
            .thenAccept { message ->
                if (client.config.auto_delete.bot.content_generation) {
                    this.launch { bot(message, client) }
                }
            }
    }
}