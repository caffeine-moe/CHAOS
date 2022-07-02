package org.caffeine.chaos.api.client

import kotlinx.serialization.Serializable

@Serializable
data class ClientGuild(
    val name : String = "",
    val id : String = "",
    var channels : MutableList<ClientGuildChannel> = arrayListOf(),
    var members : MutableList<ClientGuildMember> = arrayListOf(),
) {
/*    suspend fun muteForever() {
        discordHTTPClient.patch("https://discord.com/api/v9/users/@me/guilds/$id/settings") {
            headers {
                append(HttpHeaders.Authorization, client.user.token)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(json.encodeToString(MuteForever(MuteConfig(null, -1), true)))
        }
    }*/
}