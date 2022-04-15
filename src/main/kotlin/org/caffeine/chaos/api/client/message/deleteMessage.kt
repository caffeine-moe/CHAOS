package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient

suspend fun deleteMessage(message: Message, config: Config){
    try {
        val response = httpclient.request("$BASE_URL/channels/${message.channel.id}/messages/${message.id}") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
        }
    }catch (e: Exception){
        println(e)
        println("deletemessage")
    }
}