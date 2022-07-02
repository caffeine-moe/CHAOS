package org.caffeine.chaos.ui

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.config

class WebUI {
    suspend fun init() = coroutineScope {
        if (!config.web_ui.enabled) {
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