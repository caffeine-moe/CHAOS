package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client
import org.caffeine.chaos.api.utils.discordHTTPClient

suspend fun deleteMessage(message : Message) {
    try {
        discordHTTPClient.request("$BASE_URL/channels/${message.channel_id}/messages/${message.id}") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, client.user.token)
                append(HttpHeaders.ContentType, "application/json")
            }
        }
    } catch (e : Exception) {
        return
    }
}