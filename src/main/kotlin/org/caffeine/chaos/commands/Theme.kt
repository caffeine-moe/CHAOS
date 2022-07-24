package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.ThemeType


class Theme : Command(arrayOf("theme", "dth"), CommandInfo("Theme", "theme <Theme>", "Changes your discord theme.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
           val err : String = if (args.isNotEmpty()) {
                val theme = when (args.first().lowercase()) {
                    "d", "dark" -> ThemeType.DARK
                    "l", "light" -> ThemeType.LIGHT
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
            event.message.channel.sendMessage(error(client, event, err, commandInfo)).thenAccept {
                this.launch {
                    onComplete(it, client, true)
                }
            }
        }
}