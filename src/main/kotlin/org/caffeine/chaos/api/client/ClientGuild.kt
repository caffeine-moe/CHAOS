package org.caffeine.chaos.api.client

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.message.MessageChannel
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.token


@Serializable
data class ClientGuild(
    val name: String,
    val id: String,
) {
    suspend fun muteForever() {
        discordHTTPClient.patch("https://discord.com/api/v9/users/@me/guilds/$id/settings") {
            headers {
                append(HttpHeaders.Authorization, token)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(json.encodeToString(MuteForever(MuteConfig(null, -1), true)))
        }
    }

    suspend fun getChannels(): List<MessageChannel> {
        val re = discordHTTPClient.get("https://discord.com/api/v9/guilds/$id/channels") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        return json.decodeFromString(re.bodyAsText())
    }
}