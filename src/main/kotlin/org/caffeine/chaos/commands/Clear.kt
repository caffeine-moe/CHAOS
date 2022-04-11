package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.caffeine.chaos.loginPrompt
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

/*fun Clear(cli: Cli, event: messageCreate, config: Config) {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
    thread {
        if (event.d.content.lowercase() == ("${config.prefix}clear")) {
            print("\u001b[H\u001b[2J\u001B[38;5;255m")
            loginPrompt(cli, config)
        }
    }
}*/
