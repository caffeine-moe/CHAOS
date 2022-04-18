package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.JsonObject

suspend fun sendJsonRequest(connection: Connection, request: String) {
    try {
        connection.ws.send(request)
    }catch (e: Exception){
        println(e)
        e.printStackTrace()
        println(e.cause)
        println("sendjsonrequest")
    }
}