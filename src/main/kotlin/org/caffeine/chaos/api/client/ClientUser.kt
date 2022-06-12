package org.caffeine.chaos.api.client

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageChannel
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.handlers.CustomStatus
import org.caffeine.chaos.api.json
import java.util.concurrent.CompletableFuture
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class ClientUser(
    val verified : Boolean,
    override val username : String,
    override val discriminator : String,
    override val id : String,
    val email : String?,
    val bio : String?,
    val customStatus : CustomStatus,
    val status : String,
    override val avatar : String?,
    val relationships : ClientRelationships,
    val guilds : ClientGuilds,
    val channels : ClientChannels,
    val client : Client,
) : DiscordUser() {
    override val discriminatedName = "$username#$discriminator"

    suspend fun setHouse(house : DiscordHypeSquadHouse) {
        val houseid = when (house) {
            DiscordHypeSquadHouse.NONE -> 0
            DiscordHypeSquadHouse.BRAVERY -> 1
            DiscordHypeSquadHouse.BRILLIANCE -> 2
            DiscordHypeSquadHouse.BALANCE -> 3
        }
        if (house == DiscordHypeSquadHouse.NONE) {
            discordHTTPClient.request("$BASE_URL/hypesquad/online") {
                method = HttpMethod.Delete
                headers {
                    append(HttpHeaders.Authorization, client.config.token)
                }
            }
            return
        }
        val req = discordHTTPClient.request("$BASE_URL/hypesquad/online") {
            method = HttpMethod.Post
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
            setBody(json.encodeToString(json.parseToJsonElement("{\"house_id\":$houseid}")))
        }
        println(req.request.content.toString())
    }

    suspend fun setCustomStatus(status : String) {
        discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"custom_status\":{\"text\":\"$status\"}}").toString())
        }
    }

    suspend fun setTheme(theme : DiscordTheme) {
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

    suspend fun setStatus(status : ClientStatusType) {
        val ststr = when (status) {
            ClientStatusType.ONLINE -> "online"
            ClientStatusType.IDLE -> "idle"
            ClientStatusType.DND -> "dnd"
            ClientStatusType.INVISIBLE -> "invisible"
        }
        discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append(HttpHeaders.Authorization, client.config.token)
                append("Content-Type", "application/json")
            }
            setBody(json.encodeToString(ClientStatus(ststr)))
        }
    }

    suspend fun sendMessage(channel : MessageChannel, message : Message) : CompletableFuture<Message> {
        return org.caffeine.chaos.api.client.message.sendMessage(channel, message)
    }

    suspend fun redeemCode(code : String) : CompletableFuture<ClientUserRedeemedCode> {
        var rc = ClientUserRedeemedCode()
        var la : Long
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
        } catch (ex : Exception) {
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

    suspend fun validateChannelId(id : String) : Boolean {
        var stat : Boolean
        try {
            val response = discordHTTPClient.request("$BASE_URL/channels/${id}/messages?limit=1") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, client.config.token)
                    append("Content-Type", "application/json")
                }
            }
            stat = response.status.isSuccess()
        } catch (e : ClientRequestException) {
            stat = false
        }
        return stat
    }

    @kotlinx.serialization.Serializable
    private data class Type(
        val type : Int,
    )

    suspend fun block(userid : String) {
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