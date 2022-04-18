package org.caffeine.chaos.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.handlers.messagecreate

@Serializable
data class response(
    val op: Int?,
    val s: Int?,
    val t: String?
)

suspend fun receiveJsonRequest(payload: String, config: Config, connection: Connection, client: Client) {
    val event = Json{ignoreUnknownKeys = true}.decodeFromString<response>(payload)
    when (event.op){
        0 ->{
            when (event.t){
                "READY" ->{
                    ready(client, config, connection, payload)
                }
                "MESSAGE_CREATE" ->{
                    messagecreate(payload, config, client)
                }
            }
        }
        7 ->{
            connection.reconnect(config, connection.sid, connection.lasts, client)
        }
    }
}

