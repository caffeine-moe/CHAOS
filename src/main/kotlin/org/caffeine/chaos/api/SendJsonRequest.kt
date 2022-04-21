package org.caffeine.chaos.api

import io.ktor.websocket.*

suspend fun sendJsonRequest(connection: Connection, request: String) {
    try {
        connection.ws.send(request)
    } catch (e: Exception) {
        println(e)
        e.printStackTrace()
        println(e.cause)
        println("sendjsonrequest")
    }
}