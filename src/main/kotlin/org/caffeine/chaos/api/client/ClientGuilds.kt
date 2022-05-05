package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

@kotlinx.serialization.Serializable
class ClientGuilds(val client: Client) {
    suspend fun getAmount(): Int {
        var number = 0
        val response = discordHTTPClient.request("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientGuild>>(response.body())
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }

    suspend fun getList(): List<ClientGuild> {
        val response = discordHTTPClient.request("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        return json.decodeFromString(response.body())
    }

    suspend fun getListAsString(): String {
        val sb = StringBuilder()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val list = json.decodeFromString<List<ClientGuild>>(response.body())
        for (item: ClientGuild in list){
            sb.appendLine("${item.name} : ${item.id}")
        }
        return sb.toString()
    }
}