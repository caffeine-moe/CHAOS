package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.processes.configWatcher
import org.caffeine.chaos.processes.loginPrompt

suspend fun handleReady(client : Client) {
    log("${ConsoleColour.BLUE.value}Welcome to CHAOS!")
    clear()
    registerCommands()
    loginPrompt(client.user)
    configWatcher(client)
}
