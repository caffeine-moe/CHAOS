package org.caffeine.chaos.ui

import kotlinx.coroutines.*
import org.caffeine.chaos.api.discord.client.Client

class WebUI {
    suspend fun init(client : Client) = coroutineScope {
        if (!client.config.web_ui.enabled) {
            return@coroutineScope
        }
/*        launch {
            embeddedServer(CIO, port = 8080) {
                install(WebSockets)
                routing {
                    webSocket("/chaos") {
                        for (frame in incoming) {
                            println("amog " + frame.data.decodeToString())
                        }
                    }
                }
            }.start(wait = true)
        }*/
    }
}