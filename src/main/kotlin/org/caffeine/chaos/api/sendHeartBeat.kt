package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.log

@kotlinx.serialization.Serializable
private data class Heartbeat(
    val op: Int,
    val d: String
)

var br = false

private val heartbeat = JsonObject(
    mapOf(
        "op" to JsonPrimitive(1),
        "d" to JsonPrimitive("null"),
    )
)

suspend fun sendHeartBeat (interval: Long, connection: Connection){
    log("Heartbeat started.", "API:")
    while (!br) {
        delay(interval)
        sendJsonRequest(connection, heartbeat.toString())
    }
}
