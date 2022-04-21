package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.version

val commandlist = arrayOf(
    "ping",
    "help",
    "cmds",
    "commands",
    "ip",
    "purge",
    "sclear",
    "spurge",
    "spam",
    "sspam",
    "backup",
    "avatar",
    "av",
    "exchange",
    "crypto",
    "coin",
    "clear",
    "token",
    "q",
    "quit",
    "selfdestruct",
    "lgdm",
    "leavegroups",
    "idle",
    "away",
    "dnd",
    "donotdisturb",
    "invisible",
    "offline",
    "online",
)

suspend fun help(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == "${config.prefix}help" || event.message.content.lowercase() == "${config.prefix}cmds" || event.message.content.lowercase() == "${config.prefix}commands") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**CHAOS v$version**")
            .appendLine("**Commands:** https://caffeine.moe/CHAOS/commands/")
            .build(), config, client
        ).thenAccept { message ->
            this.launch {
                bot(message, config)
            }
        }
    }
}
