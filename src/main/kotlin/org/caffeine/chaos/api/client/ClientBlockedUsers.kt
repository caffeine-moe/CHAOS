package org.caffeine.chaos.api.client

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

@kotlinx.serialization.Serializable
class ClientBlockedUsers(val client: Client) {
    suspend fun getList(): List<DiscordUser> {
        val blocked = mutableListOf<DiscordUser>()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientRelationship>>(response.bodyAsText())
        for (blockedUser in final) {
            if (blockedUser.type == 2) {
                val user = blockedUser.user
                blocked.add(DiscordUser(user.username, user.discriminator, user.id, user.avatar))
            }
        }
        return blocked
    }
}