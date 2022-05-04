package org.caffeine.chaos.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
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
    val event = json.decodeFromString<DefaultResponse>(payload)
    if (event.s != null && event.s > 0) {
        seq = event.s
    }
    when (event.op) {
        0 -> {
            when (event.t) {
                "READY" -> {
                    ready(client, payload)
                }
                "MESSAGE_CREATE" -> {
                    if (ready) {
                        messageCreate(payload, client)
                    }
                }
            }
        }
        7 -> {
            connection.recRes(sid, seq, client)
        }
        11 -> {
        }
        else -> {
            println(payload)
        }
    }
}

