package org.caffeine.chaos.api.client

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageChannel
import org.caffeine.chaos.api.httpclient
import java.util.concurrent.CompletableFuture

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

    suspend fun setStatus(status: ClientStatusType) {
        when (status) {
            ClientStatusType.ONLINE -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("online")))
                }
            }
            ClientStatusType.IDLE -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("idle")))
                }
            }
            ClientStatusType.DND -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("dnd")))
                }
            }
            ClientStatusType.INVISIBLE -> {
                val rq = httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("invisible")))
                }
                println(rq.request.headers.toString())
            }
        }
    }

    suspend fun sendMessage(channel: MessageChannel, message: Message): CompletableFuture<Message> {
        return org.caffeine.chaos.api.client.message.sendMessage(channel, message, channel.client)
    }

    suspend fun redeemCode(code: String) {
        val rq = httpclient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
            expectSuccess = true
        }
    }

    suspend fun validateChannelId(id: String): Boolean {
        var valid = false
        val response = httpclient.request("$BASE_URL/channels/${id}/messages?limit=1") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
        }
        if (response.status.isSuccess()) {
            valid = true
        }
        return valid
    }
}