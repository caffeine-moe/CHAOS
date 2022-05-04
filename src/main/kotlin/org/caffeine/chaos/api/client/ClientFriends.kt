package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

data class ClientFriends(val client: Client) {
    suspend fun getAmount(): Int {
        var number = 0
        val response = discordHTTPClient.request("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientFriend>>(response.body())
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }

    suspend fun getList(): StringBuilder {
        val sb = StringBuilder()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientFriend>>(response.body())
        for ((count) in final.withIndex()) {
            sb.appendLine("${final[count].user.username}#${final[count].user.discriminator}")
        }
        return sb
    }
}