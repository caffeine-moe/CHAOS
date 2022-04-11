package org.caffeine.chaos.commands

import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import org.caffeine.chaos.Config
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

/*
suspend fun SelfDestruct(cli: Cli, event: messageCreate, config: Config, ws: DefaultClientWebSocketSession) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.d.content.lowercase() == "${config.prefix}quit" || event.d.content.lowercase() == "${config.prefix}q" || event.d.content.lowercase() == "${config.prefix}selfdestruct") {
            ws.close()
            exitProcess(69)
    }
}*/
