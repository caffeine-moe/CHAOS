package org.caffeine.chaos.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.handlers.messageCreate
import org.caffeine.chaos.api.handlers.ready
import org.caffeine.chaos.config.Config

@Serializable
data class DefaultResponse(
    val op: Int?,
    val s: Int?,
    val t: String?,
)

var ready = false

suspend fun receiveJsonRequest(payload: String, connection: Connection, client: Client, config: Config) {
    val event = Json { ignoreUnknownKeys = true }.decodeFromString<DefaultResponse>(payload)
    when (event.op) {
        0 -> {
            when (event.t) {
                "READY" -> {
                    ready(client, config, connection, payload)
                }
                "MESSAGE_CREATE" -> {
                    if (ready) {
                        messageCreate(payload, client)
                    }
                }
            }
        }
        7 -> {
            connection.reconnect(config, connection.sid, connection.lasts, client)
        }
    }
}

