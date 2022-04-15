package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.caffeine.chaos.LogV2

private val heartbeat = JsonObject(
    mapOf(
        "op" to JsonPrimitive(1),
        "d" to JsonPrimitive("null"),
    )
)

suspend fun sendHeartBeat (interval: Long, ws: DefaultClientWebSocketSession){
    LogV2("Heartbeat started.", "API:")
    while (true) {
        delay(interval)
        sendJsonRequest(ws, heartbeat)
    }
}