package org.caffeine.chaos.api.client

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageChannel
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json
import java.util.concurrent.CompletableFuture
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class ClientUser(
    val verified: Boolean,
    override val username: String,
    override val discriminator: String,
    override val id: String,
    val email: String?,
    val bio: String?,
    override val avatar: String?,
    val relationships: ClientRelationships,
    val guilds: ClientGuilds,
    val channels: ClientChannels,
    val client: Client,
) : DiscordUser() {
    override val discriminatedName = "$username#$discriminator"

    suspend fun setTheme(theme: DiscordTheme) {
        val thstr = when (theme) {
            DiscordTheme.DARK -> "dark"
            DiscordTheme.LIGHT -> "light"
        }
        discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
            setBody(json.encodeToString(ClientTheme(thstr)))
        }
    }

    suspend fun setStatus(status: ClientStatusType) {
        when (status) {
            ClientStatusType.ONLINE -> {
                discordHTTPClient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(json.encodeToString(ClientStatus("online")))
                }
            }
            ClientStatusType.IDLE -> {
                discordHTTPClient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(json.encodeToString(ClientStatus("idle")))
                }
            }
            ClientStatusType.DND -> {
                discordHTTPClient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(json.encodeToString(ClientStatus("dnd")))
                }
            }
            ClientStatusType.INVISIBLE -> {
                discordHTTPClient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, client.config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(json.encodeToString(ClientStatus("invisible")))
                }
            }
        }
    }

    suspend fun sendMessage(channel: MessageChannel, message: Message): CompletableFuture<Message> {
        return org.caffeine.chaos.api.client.message.sendMessage(channel, message)
    }

    suspend fun redeemCode(code: String): CompletableFuture<ClientUserRedeemedCode> {
        var rc = ClientUserRedeemedCode()
        var la: Long
        val start = System.currentTimeMillis()
        try {
            discordHTTPClient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
                method = HttpMethod.Post
                headers {
                    append(HttpHeaders.Authorization, client.config.token)
                }
                expectSuccess = true
            }
            val end = System.currentTimeMillis()
            la = (start - end)
            rc = ClientUserRedeemedCode(code, la.absoluteValue, ClientUserRedeemedCodeStatus.SUCCESS)
        } catch (ex: Exception) {
            val end = System.currentTimeMillis()
            la = (start - end)
            if (ex.toString().contains("Unknown Gift Code")) {
                rc = ClientUserRedeemedCode(code,
                    la.absoluteValue,
                    ClientUserRedeemedCodeStatus.INVALID,
                    ClientUserRedeemedCodeError.UNKNOWN_CODE)
            }
        }
        return CompletableFuture.completedFuture(rc)
    }

    suspend fun validateChannelId(id: String): Boolean {
        val response = discordHTTPClient.request("$BASE_URL/channels/${id}/messages?limit=1") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
        }
        println(response)
        return true
    }

    @kotlinx.serialization.Serializable
    private data class Type(
        val type: Int,
    )

    suspend fun block(userid: String) {
        discordHTTPClient.request("$BASE_URL/users/@me/relationships/$userid") {
            method = HttpMethod.Put
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
            setBody(json.encodeToString(Type(2)))
        }
    }
}