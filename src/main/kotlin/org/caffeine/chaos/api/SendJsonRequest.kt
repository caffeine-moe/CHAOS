package org.caffeine.chaos.api

import io.ktor.websocket.*
import org.caffeine.chaos.log
import kotlin.coroutines.cancellation.CancellationException

suspend fun sendJsonRequest(connection : Connection, request : String) {
    try {
        connection.ws.send(request)
    } catch (e : CancellationException) {
        log("Websocket disconnected, reconnecting...", "API:")
        connection.reconnect()
    }
}