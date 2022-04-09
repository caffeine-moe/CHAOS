package org.caffeine.chaos.api

import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.json.JsonObject

suspend fun sendJsonRequest(ws: DefaultClientWebSocketSession, request: JsonObject) {
    try {
        ws.send(request.toString())
    }catch (e: Exception){
        println(e.cause)
        println("sendjsonrequest")
    }
}