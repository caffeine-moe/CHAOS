package org.caffeine.chaos.handlers

import org.caffeine.chaos.processes.configWatcher
import org.caffeine.chaos.processes.loginPrompt
import org.caffeine.chaos.utils.clear
import org.caffeine.octane.client.Client
import org.caffeine.octane.utils.ConsoleColour
import org.caffeine.octane.utils.log

suspend fun handleReady(client : Client) {
    log("${ConsoleColour.BLUE.value}Welcome to CHAOS!", "CHAOS:")
    clear()
    registerCommands()
    loginPrompt(client.user)
    configWatcher(client)
}