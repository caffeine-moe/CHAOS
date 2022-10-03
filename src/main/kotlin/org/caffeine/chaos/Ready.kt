package org.caffeine.chaos

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.models.message.MessageSearchFilters
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.clear
import org.caffeine.chaos.api.utils.log

suspend fun ready(client : Client) {
    log("${ConsoleColours.BLUE.value}Welcome to CHAOS!")
    clear()
    registerCommands()
    loginPrompt(client)
    configWatcher(client)
}