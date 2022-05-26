package org.caffeine.chaos.ui

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.api.client.Client

class WebUI {
    suspend fun init(client: Client) = coroutineScope {
        if (client.config.web_ui.enabled) {
            return@coroutineScope
        }
    }
}