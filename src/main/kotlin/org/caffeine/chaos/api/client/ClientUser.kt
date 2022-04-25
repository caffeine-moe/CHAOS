package org.caffeine.chaos.api.client

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient

data class ClientUser(
    val verified: Boolean,
    val username: String,
    val discriminator: String,
    val id: String,
    val email: String?,
    val bio: String?,
    val avatar: String?,
    val friends: ClientFriends,
    val guilds: ClientGuilds,
    val channels: ClientChannels,
    val client: Client,
) {
    val discriminatedName = "$username#$discriminator"
    fun avatarUrl(): String {
        var av = ""
        if (!avatar.isNullOrBlank()) {
            av = "https://cdn.discordapp.com/avatars/$id/$avatar"
        }
        return av
    }

    suspend fun redeemCode(code: String) {
        httpclient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
            expectSuccess = true
        }
    }
}