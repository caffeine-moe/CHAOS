package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.client.user.ClientUser
import org.caffeine.octane.typedefs.ThemeType
import org.caffeine.octane.utils.awaitThen

class Theme : Command(arrayOf("theme", "dth"), CommandInfo("Theme", "theme <Theme>", "Changes your discord theme.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (client.user !is ClientUser) return
        val err : String = if (args.isNotEmpty()) {
            val theme = when (args.first().lowercase()) {
                "d", "dark" -> ThemeType.DARK
                "l", "light" -> ThemeType.LIGHT
                else -> null
            }
            if (theme != null) {
                (client.user as ClientUser).setTheme(theme)
                return
            }
            "${args.joinToString(" ")} is not a valid theme!"
        } else {
            "No arguments passed for theme."
        }
        event.message.channel.sendMessage(error(client, event, err, commandInfo)).awaitThen {
            onComplete(it, true)
        }
    }
}