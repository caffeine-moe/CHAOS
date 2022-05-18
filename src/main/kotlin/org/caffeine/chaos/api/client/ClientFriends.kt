package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

@kotlinx.serialization.Serializable
data class ClientFriends(val client: Client) {
    suspend fun getAmount(): Int {
        var number = 0
        val response = discordHTTPClient.request("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientRelationship>>(response.body())
        for (friend in final) {
            if (friend.type == 1) {
                number++
            }
        }
        return number
    }

    suspend fun getList(): List<ClientFriend> {
        val friends = mutableListOf<ClientFriend>()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientRelationship>>(response.bodyAsText())
        for (relationship in final) {
            if (relationship.type == 1) {
                val user = relationship.user
                val friend = ClientFriend(user.username, user.discriminator, user.id, user.avatar)
                friends.add(friend)
            }
        }
        val jsone = json.encodeToJsonElement(friends)
        return json.decodeFromJsonElement(jsone)
    }
}