package org.caffeine.chaos.api

import io.ktor.websocket.*
import org.caffeine.chaos.log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.cancellation.CancellationException

suspend fun sendJsonRequest(connection : Connection, request : String) {
    try {
        connection.ws.send(request)
    } catch (e : Exception) {
        if (e is CancellationException) {
            log("Websocket disconnected, reconnecting...", "API:")
            connection.reconnect()
        }
        println(e)
        e.printStackTrace()
        println(e.cause)
        println("sendjsonrequest")
        println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss")))
    }
}