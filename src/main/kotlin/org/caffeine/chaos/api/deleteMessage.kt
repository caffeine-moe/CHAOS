package org.caffeine.chaos.api

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config

suspend fun deleteMessage(channel: String, message: String, config: Config){
    try {
        val response = httpclient.request<String>("$BASE_URL/channels/$channel/messages/$message") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
        }
    }catch (e: Exception){
        println(e.cause)
        println("deletemessage")
    }
}