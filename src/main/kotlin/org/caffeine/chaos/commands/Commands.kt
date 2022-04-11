package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.caffeine.chaos.version
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

val commandlist = arrayOf(
    "ping",
    "help",
    "cmds",
    "commands",
    "ip",
    "purge",
    "sclear",
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
    "online"
)

/*
fun Help(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        val spc = "    "
        if (event.messageContent.lowercase() == "${config.prefix}help" || event.messageContent.lowercase() == "${config.prefix}cmds" || event.messageContent.lowercase() == "${config.prefix}commands") {
            event.channel.sendMessage(
                "CSB $version\nCommands: https://docs.caffeine.moe/CSB/Commands/"
            ).thenAccept { }
        }
    }
}*/
