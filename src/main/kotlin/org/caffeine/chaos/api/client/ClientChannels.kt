package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

@kotlinx.serialization.Serializable
data class ClientChannels(val client : Client) {
    val groupChannels = ClientGroupChannels(client)
    suspend fun getAmount() : Int {
        var number = 0
        val response = discordHTTPClient.request("$BASE_URL/users/@me/channels") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        println(response)
        val final = json.decodeFromString<List<ClientChannel>>(response.body())
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }

    suspend fun getList() : StringBuilder {
        val sb = StringBuilder()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/channels") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        println(response)
        val final = json.decodeFromString<List<ClientChannel>>(response.body())
        for ((count) in final.withIndex()) {
            sb.appendLine(final[count].id)
        }
        return sb
    }
}