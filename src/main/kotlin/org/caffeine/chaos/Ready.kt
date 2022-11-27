package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : Client) {
    log("${ConsoleColour.BLUE.value}Welcome to CHAOS!")
    clear()
    registerCommands()
    loginPrompt(client.user)
    configWatcher(client)
}
