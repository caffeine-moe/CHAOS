package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent

suspend fun handleEvent(client : Client, event : ClientEvent) {
    when (event) {
        is ClientEvent.Ready -> {
            ready(client)
        }

        is ClientEvent.MessageCreate -> {
            handleMessage(event, client)
        }
    }
}