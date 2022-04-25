package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient

class ClientGuilds(val client: Client) {
    suspend fun getAmount(): Int {
        var number = 0
        val response = httpclient.request("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<ClientGuild>>(response.body())
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }

    suspend fun getList(): StringBuilder {
        val sb = StringBuilder()
        val response = httpclient.request("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<ClientGuild>>(response.body())
        for ((count) in final.withIndex()) {
            sb.appendLine(final[count].name)
        }
        return sb
    }
}