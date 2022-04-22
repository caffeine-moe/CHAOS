package org.caffeine.chaos.api.client

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.config.Config

data class ClientUser(
        val verified: Boolean,
        val username: String,
        val discriminator: Int,
        val id: String,
        val email: String,
        val bio: String?,
        val avatar: String?,
        val friends: ClientFriends,
        val guilds: ClientGuilds,
        val channels: ClientChannels,
) {
    val discriminatedName = "$username#$discriminator"
    fun avatarUrl(): String {
        var av = ""
        if (!avatar.isNullOrBlank()) {
            av = "https://cdn.discordapp.com/avatars/$id/$avatar"
        }
        return av
    }

    suspend fun redeemCode(code: String, config: Config) {
        httpclient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
            expectSuccess = true
        }
    }
}