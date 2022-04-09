package org.caffeine.chaos.api

import io.ktor.client.features.websocket.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.commandHandler

@Serializable
data class messageCreate(
    val d: D,
    val op: Int?,
    val s: Int?,
    val t: String?
)

@Serializable
data class D(
    val attachments: List<String>?,
    val author: Author,
    val channel_id: String,
    val components: List<String>?,
    val content: String,
    val edited_timestamp: String?,
    val embeds: List<String>?,
    val flags: Int?,
    val id: String?,
    val mention_everyone: Boolean?,
    val mention_roles: List<String>?,
    val mentions: List<String>?,
    val nonce: String?,
    val pinned: Boolean?,
    val referenced_message: String?,
    val timestamp: String?,
    val tts: Boolean?,
    val type: Int?
)

@Serializable
data class Author(
    val avatar: String?,
    val discriminator: String?,
    val id: String?,
    val public_flags: Int?,
    val username: String?
)

@Serializable
data class response(
    val op: Int?,
    val s: Int?,
    val t: String?
)

data class readypayload(
    var payload: String = ""
)

@OptIn(ExperimentalSerializationApi::class)
suspend fun recieveJsonRequest(payload: String, config: Config, ws: DefaultClientWebSocketSession) {
    println(payload)
    val event = Json{ignoreUnknownKeys = true}.decodeFromString<response>(payload)
    when (event.op){
        0 ->{
            when (event.t){
                "READY" ->{
                    payload.also { readypayload().payload = it }
                    println("set payload")
                    println(readypayload().payload)
                    onReady(payload, config, ws)
                }
                "MESSAGE_CREATE" ->{
                    val event = Json { ignoreUnknownKeys = true }.decodeFromString<messageCreate>(payload)
                    commandHandler(config, event, ws)
                }
            }
        }
        7 ->{
            println(payload)
        }
    }
}

