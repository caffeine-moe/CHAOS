package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.DiscordTheme
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Theme : Command(arrayOf("theme", "dth")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            val err: String = if (args.isNotEmpty()) {
                val theme = when (args.first()) {
                    "d", "dark" -> DiscordTheme.DARK
                    "l", "light" -> DiscordTheme.LIGHT
                    else -> null
                }
                if (theme != null) {
                    client.user.setTheme(theme)
                    return@coroutineScope
                }
                "${args.joinToString(" ")} is not a valid theme!"
            } else {
                "No arguments passed for theme."
            }
            event.channel.sendMessage(MessageBuilder()
                .appendLine("**Incorrect usage:** '${event.message.content}'")
                .appendLine("**Error:** $err")
                .appendLine("**Correct usage:** `${client.config.prefix}theme Theme`")
                .build()
            ).thenAccept {
                this.launch {
                    onComplete(it, client, true)
                }
            }
        }
}