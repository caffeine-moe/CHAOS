package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client

suspend fun ready(client: Client) {
    log("\u001B[38;5;33mWelcome to CHAOS!")
    clear()
    registerCommands()
    loginPrompt(client)
    configWatcher(client)
}