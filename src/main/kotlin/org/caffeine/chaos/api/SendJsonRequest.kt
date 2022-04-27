package org.caffeine.chaos.api

import io.ktor.websocket.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

suspend fun sendJsonRequest(connection: Connection, request: String) {
    try {
        connection.ws.send(request)
    } catch (e: Exception) {
        println(e)
        e.printStackTrace()
        println(e.cause)
        println("sendjsonrequest")
        println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss")))
    }
}