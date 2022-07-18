package org.caffeine.chaos.api.client.user

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.models.User
import org.caffeine.chaos.api.models.channels.BaseChannel
import org.caffeine.chaos.api.typedefs.*
import java.util.concurrent.CompletableFuture
import kotlin.math.absoluteValue

data class ClientUser(
    val verified : Boolean,
    override val username : String,
    override val discriminator : String,
    override val id : String,
    val email : String?,
    val bio : String?,
    val settings : ClientUserSettings,
    override val avatar : String?,
    //val relationships : ClientUserRelationships,
    //val channels: HashMap<String, BaseChannel>,
    val premium : Boolean,
    val token : String,
    val client : Client,
) : DiscordUser {

    private val _guilds = HashMap<String, Guild>()

    val guilds : Map<String, Guild>
        get() = _guilds


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
            setBody(json.parseToJsonElement("{\"status\":\"${status.value}\"}").toString())
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

    suspend fun redeemCode(code : String) : CompletableFuture<RedeemedCode> {
        var rc = RedeemedCode()
        var la : Long
        val start = System.currentTimeMillis()
        try {
            client.utils.discordHTTPClient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
                method = HttpMethod.Post
                expectSuccess = true
            }
            val end = System.currentTimeMillis()
            la = (start - end)
            rc = RedeemedCode(code, la.absoluteValue, RedeemedCodeStatusType.SUCCESS)
        } catch (ex : Exception) {
            val end = System.currentTimeMillis()
            la = (start - end)
            if (ex.toString().contains("Unknown Gift Code")) {
                rc = RedeemedCode(code,
                    la.absoluteValue,
                    RedeemedCodeStatusType.INVALID,
                    RedeemedCodeErrorType.UNKNOWN_CODE)
            }
        }
        return CompletableFuture.completedFuture(rc)
    }

    suspend fun block(user : User) {
        client.utils.discordHTTPClient.request("$BASE_URL/users/@me/relationships/${user.id}") {
            method = HttpMethod.Put
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"type\":2}").toString())
        }
    }

    fun addGuild(guild : Guild) {
        _guilds[guild.id] = guild
    }

    fun removeGuild(guild : Guild) {
        _guilds.remove(guild.id)
    }

    fun setGuilds(guilds : Map<String, Guild>) {
        _guilds.clear()
        _guilds.putAll(guilds)
    }
}