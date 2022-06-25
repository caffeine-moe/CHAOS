package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client

suspend fun ready(client : Client) = coroutineScope {
    log("\u001B[38;5;33mWelcome to CHAOS!")
    clear()
    registerCommands()
    loginPrompt(client)
    launch {
        configWatcher(client)
    }
}