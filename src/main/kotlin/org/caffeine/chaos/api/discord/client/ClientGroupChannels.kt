package org.caffeine.chaos.api.discord.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.utils.discordHTTPClient
import org.caffeine.chaos.api.json

data class ClientGroupChannels(val client : Client) {
    suspend fun getList() : MutableList<ClientChannel> {
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
                val ccr = mutableListOf(ClientChannelRecipient(
                    final[count].recipients[count].id,
                    final[count].recipients[count].username,
                    final[count].recipients[count].avatar,
                    final[count].recipients[count].discriminator,
                    final[count].recipients[count].public_flags,
                    final[count].recipients[count].bot
                ))
                val cc = ClientChannel(
                    final[count].id,
                    final[count].type,
                    final[count].last_message_id,
                    ccr,
                    final[count].name,
                    final[count].icon,
                    final[count].owner_id,
                )
                list.add(cc)
            }
        }
        return list
    }
}