package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.JsonObject

suspend fun sendJsonRequest(ws: DefaultClientWebSocketSession, request: JsonObject) {
    try {
        ws.send(request.toString())
    }catch (e: Exception){
        println(e.cause)
        println("sendjsonrequest")
    }
}