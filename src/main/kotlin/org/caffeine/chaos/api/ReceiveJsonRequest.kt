package org.caffeine.chaos.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.handlers.messageCreate
import org.caffeine.chaos.api.handlers.ready

@Serializable
data class DefaultResponse(
    val op: Int?,
    val s: Int?,
    val t: String?,
)

var ready = false

suspend fun receiveJsonRequest(payload: String, connection: Connection, client: Client) {
    val event = Json { ignoreUnknownKeys = true }.decodeFromString<DefaultResponse>(payload)
    when (event.op) {
        0 -> {
            connection.seq = event.s!!
            when (event.t) {
                "READY" -> {
                    ready(client, connection, payload)
                }
                "MESSAGE_CREATE" -> {
                    if (ready) {
                        messageCreate(payload, client)
                    }
                }
            }
        }
        7 -> {
            connection.reconnect(connection.sid, connection.seq, client)
        }
    }
}

