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
    suspend fun getList(): List<ClientBlockedUser> {
        val blocked = mutableListOf<ClientBlockedUser>()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientRelationship>>(response.bodyAsText())
        for (i in final) {
            if (i.type == 2) {
                val user = ClientBlockedUser(
                    i.user.username,
                    i.user.discriminator,
                    i.user.avatar,
                    i.user.id
                )
                blocked.add(user)
            }
        }
        return blocked
    }
}