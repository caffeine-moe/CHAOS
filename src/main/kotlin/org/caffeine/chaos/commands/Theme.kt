package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Theme : Command(arrayOf("theme", "dth"), CommandInfo("Theme", "theme <Theme>", "Changes your discord theme.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            val err : String = if (args.isNotEmpty()) {
                val theme = when (args.first().lowercase()) {
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
            event.channel.sendMessage(error(client, event, err, commandInfo)).thenAccept {
                this.launch {
                    onComplete(it, client, true)
                }
            }*/
        }
}