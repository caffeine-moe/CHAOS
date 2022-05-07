package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json

@kotlinx.serialization.Serializable
data class ClientGroupChannels(val client: Client) {
    suspend fun getList(): MutableList<ClientChannel> {
        val list = mutableListOf<ClientChannel>()
        val response = discordHTTPClient.request("$BASE_URL/users/@me/channels") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
        val final = json.decodeFromString<List<SerialClientChannel>>(response.body())
        for ((count) in final.withIndex()) {
            if (final[count].type == 3) {
                val cc = ClientChannel(
                    final[count].id,
                    final[count].type,
                    final[count].last_message_id,
                    final[count].recipients,
                    final[count].name,
                    final[count].icon,
                    final[count].owner_id,
                    client
                )
                list.add(cc)
            }
        }
        return list
    }
}