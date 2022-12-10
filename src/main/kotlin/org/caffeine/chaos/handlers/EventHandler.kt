package org.caffeine.chaos.handlers

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent

suspend fun handleEvent(client : Client, event : ClientEvent) {
    when (event) {
        is ClientEvent.Ready -> {
            handleReady(client)
        }

        is ClientEvent.MessageCreate -> {
            handleMessage(event, client)
        }
    }
}