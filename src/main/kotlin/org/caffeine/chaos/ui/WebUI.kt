package org.caffeine.chaos.ui

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.api.client.Client

class WebUI {
    suspend fun init(client: Client) = coroutineScope {
        if (!client.config.web_ui.enabled) { return@coroutineScope }
        serv()
    }
    private suspend fun serv() = coroutineScope {
        embeddedServer(CIO, port = 7270) {
            routing {
                get("/") {
                    call.respondText("Hello, world!")
                }
            }
        }.start(wait = true)
    }
}