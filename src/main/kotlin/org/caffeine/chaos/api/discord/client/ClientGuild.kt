package org.caffeine.chaos.api.discord.client

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.utils.discordHTTPClient
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.token


@Serializable
data class ClientGuild(
    val name : String = "",
    val id : String = "",
    var channels : MutableList<ClientGuildChannel> = arrayListOf(),
    var members : MutableList<ClientGuildMember> = arrayListOf(),
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
}