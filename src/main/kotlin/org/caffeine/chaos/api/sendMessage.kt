package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config
import org.caffeine.chaos.commands.bot
import org.caffeine.chaos.commands.status
import java.time.ZoneOffset.UTC
import java.util.Date
import java.util.TimeZone

@kotlinx.serialization.Serializable
data class messageSend(
    val content: String,
    val nonce: String,
    val tts: Boolean = false
)

@kotlinx.serialization.Serializable
data class sendMessageResponse(
    val attachments: List<String>,
    val author: sendMessageResponseAuthor,
    val channel_id: String,
    val components: List<String>,
    val content: String,
    val edited_timestamp: String?,
    val embeds: List<String>,
    val flags: Int,
    val id: String,
    val mention_everyone: Boolean,
    val mention_roles: List<String>,
    val mentions: List<String>,
    val nonce: String,
    val pinned: Boolean,
    val referenced_message: String,
    val timestamp: String,
    val tts: Boolean,
    val type: Int
)

@kotlinx.serialization.Serializable
data class sendMessageResponseAuthor(
    val avatar: String,
    val discriminator: String,
    val id: String,
    val public_flags: Int,
    val username: String
)

suspend fun sendMessage(id: String, content: String, config: Config){
    try {
        val response = httpclient.request<String>("$BASE_URL/channels/$id/messages") {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
            body = Json.encodeToJsonElement(messageSend(content, System.currentTimeMillis().toString()))
        }
    }catch (e: Exception){
        println(e)
        println("sendmessage")
    }
}