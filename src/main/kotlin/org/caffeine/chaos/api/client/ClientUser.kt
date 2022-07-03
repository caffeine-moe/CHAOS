package org.caffeine.chaos.api.client

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.handlers.CustomStatus
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.interfaces.IDiscordUser
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.models.channels.BaseChannel
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.typedefs.ThemeType
import org.caffeine.chaos.api.typedefs.StatusType
import java.util.concurrent.CompletableFuture
import kotlin.math.absoluteValue

data class ClientUser(
    val verified : Boolean,
    override val username : String,
    override val discriminator : String,
    override val id : String,
    val email : String?,
    val bio : String?,
    val customStatus : CustomStatus,
    val status : StatusType,
    override val avatar : String?,
    //val relationships : ClientRelationships,
    //val channels: HashMap<String, BaseChannel>,
    //val guilds : HashMap<String, Guild>,
    val premium : Boolean,
    val token : String,
    val client : Client,
) : IDiscordUser {

    override val discriminatedName = "$username#$discriminator"

    override fun avatarUrl() : String {
        return if (!avatar.isNullOrBlank()) {
            if (avatar.startsWith("a_")) {
                "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/$id/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }

/*    fun getGuild(channel : MessageChannel) : ClientGuild? {
        var guild : ClientGuild? = null
        for (g in client.user.guilds) {
            for (c in g.channels) {
                if (c.id == channel.id) {
                    guild = g
                }
            }
        }
        return guild
    }*/

    suspend fun setHouse(house : HypeSquadHouseType) {
        if (house == HypeSquadHouseType.NONE) {
            client.utils.discordHTTPClient.request("$BASE_URL/hypesquad/online") {
                method = HttpMethod.Delete
            }
        } else {
            client.utils.discordHTTPClient.request("$BASE_URL/hypesquad/online") {
                method = HttpMethod.Post
                headers {
                    append("Content-Type", "application/json")
                }
                setBody(json.parseToJsonElement("{\"house_id\":${house.ordinal}}").toString())
            }
        }
    }

    suspend fun setCustomStatus(status : String) {
        client.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"custom_status\":{\"text\":\"$status\"}}").toString())
        }
    }

    suspend fun setTheme(theme : ThemeType) {
        client.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"theme\":\"${theme.value}\"}").toString())
        }
    }

    suspend fun setStatus(status : StatusType) {
        client.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"status\":{\"text\":\"${status.value}\"}}").toString())
        }
    }

    suspend fun sendMessage(channel : BaseChannel, message : MessageOptions) : Any {
        client.utils.discordHTTPClient.request("$BASE_URL/channels/${channel.id}/messages") {
            method = HttpMethod.Post
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.encodeToString(message))
        }
        return Any()
    }

    suspend fun redeemCode(code : String) : CompletableFuture<ClientUserRedeemedCode> {
        var rc = ClientUserRedeemedCode()
        var la : Long
        val start = System.currentTimeMillis()
        try {
            client.utils.discordHTTPClient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
                method = HttpMethod.Post
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

    suspend fun block(userId : String) {
        client.utils.discordHTTPClient.request("$BASE_URL/users/@me/relationships/$userId") {
            method = HttpMethod.Put
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"type\":2}").toString())
        }
    }
}