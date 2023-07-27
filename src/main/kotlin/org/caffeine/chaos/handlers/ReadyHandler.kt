package org.caffeine.chaos.handlers

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.processes.configWatcher
import org.caffeine.chaos.processes.loginPrompt
import org.caffeine.chaos.utils.clear

suspend fun handleReady(client : Client) {
    log("${ConsoleColour.BLUE.value}Welcome to CHAOS!", "CHAOS:")
    clear()
    registerCommands()
    loginPrompt(client.user)
    configWatcher(client)
}