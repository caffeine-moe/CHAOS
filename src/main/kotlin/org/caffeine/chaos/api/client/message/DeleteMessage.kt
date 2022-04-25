package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.httpclient

suspend fun deleteMessage(message: Message, client: Client) {
    try {
        httpclient.request("$BASE_URL/channels/${message.channel_id}/messages/${message.id}") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append(HttpHeaders.ContentType, "application/json")
            }
        }
    } catch (e: Exception) {
        println(e)
        println("deletemessage")
    }
}