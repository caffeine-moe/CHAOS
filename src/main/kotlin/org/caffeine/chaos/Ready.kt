package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : Client) = coroutineScope {
    log("${ConsoleColours.BLUE.value}Welcome to CHAOS!")
    clear()
    registerCommands()
    loginPrompt(client)
    launch {
        configWatcher(client)
    }
}