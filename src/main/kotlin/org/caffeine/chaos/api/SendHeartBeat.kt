package org.caffeine.chaos.api

import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.log

@kotlinx.serialization.Serializable
private data class Heartbeat(
    val op: Int,
    val d: String,
)

var br = false

suspend fun sendHeartBeat(interval: Long, connection: Connection) {
    br = false
    var hb = json.encodeToString(Heartbeat(1, "null"))
    log("Heartbeat started.", "API:")
    while (!br) {
        if (seq > 0) {
            hb = json.encodeToString(Heartbeat(1, "$seq"))
        }
        delay(interval)
        sendJsonRequest(connection, hb)
    }
}
