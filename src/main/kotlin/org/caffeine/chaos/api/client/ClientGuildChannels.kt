package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

@kotlinx.serialization.Serializable
data class ClientGuildChannels(val client: Client) {
    suspend fun getList(): MutableList<ClientChannel> {
        val list = mutableListOf<ClientChannel>()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/channels") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<ClientChannel>>(response.body())
        for ((count) in final.withIndex()) {
            if (final[count].type == 3) {
                list.add(final[count])
            }
        }
        return list
    }
}
