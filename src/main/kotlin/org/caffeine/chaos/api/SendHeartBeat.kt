package org.caffeine.chaos.api

import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.log

@kotlinx.serialization.Serializable
private data class Heartbeat(
    val op: Int,
    val d: String,
)

var br = false

suspend fun sendHeartBeat(interval: Long, connection: Connection) {
    br = false
    var hb = Json.encodeToString(Heartbeat(1, "null"))
    log("Heartbeat started.", "API:")
    while (!br) {
        if (connection.seq > 0) {
            hb = Json.encodeToString(Heartbeat(1, "${connection.seq}"))
        }
        delay(interval)
        sendJsonRequest(connection, hb)
    }
}
