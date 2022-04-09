package org.caffeine.chaos.api

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config

@kotlinx.serialization.Serializable
data class editContent(
    val content: String
)

suspend fun editMessage(channel: String, message: String, content: String, config: Config){
    try {
        println(message)
        val response = httpclient.request<String>("$BASE_URL/channels/$channel/messages/$message") {
            method = HttpMethod.Patch
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
            body = Json.encodeToJsonElement(editContent(content))
        }
    }catch (e: Exception){
        println(e.cause)
        println("editmessage")
    }
}