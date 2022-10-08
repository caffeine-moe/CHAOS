package org.caffeine.chaos.api.client.user

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.channels.DMChannel
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageSearchFilters
import org.caffeine.chaos.api.models.users.BlockedUser
import org.caffeine.chaos.api.models.users.Friend
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.payloads.gateway.data.SerialMessage
import org.caffeine.chaos.api.typedefs.*
import org.caffeine.chaos.api.utils.log
import kotlin.math.absoluteValue

class ClientUser(private val impl : ClientUserImpl) : BaseClientUser by impl {
    private val clientImpl = impl.clientImpl
    val dmChannels : Map<String, DMChannel>
        get() = impl._channels.values.filterIsInstance<DMChannel>().associateBy { it.id }

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

    suspend fun deleteChannel(channel : BaseChannel) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${channel.id}") {
            method = HttpMethod.Delete
        }
    }

    fun unblock(user : BlockedUser) {
        return
    }

    fun removeFriend(friend : Friend) {
        return
    }

    suspend fun setHouse(house : HypeSquadHouseType) {
        if (house == HypeSquadHouseType.NONE) {
            clientImpl.utils.discordHTTPClient.request("$BASE_URL/hypesquad/online") {
                method = HttpMethod.Delete
            }
        } else {
            clientImpl.utils.discordHTTPClient.request("$BASE_URL/hypesquad/online") {
                method = HttpMethod.Post
                headers {
                    append("Content-Type", "application/json")
                }
                setBody(json.parseToJsonElement("{\"house_id\":${house.ordinal}}").toString())
            }
        }
    }

    suspend fun setCustomStatus(status : String) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"custom_status\":{\"text\":\"$status\"}}").toString())
        }
    }

    suspend fun setTheme(theme : ThemeType) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"theme\":\"${theme.value}\"}").toString())
        }
    }

    suspend fun setStatus(status : StatusType) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"status\":\"${status.value}\"}").toString())
        }
    }

    suspend fun sendMessage(channel : BaseChannel, message : MessageOptions) : CompletableDeferred<Message> {
        val response = clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${channel.id}/messages") {
            method = HttpMethod.Post
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.encodeToString(message))
        }
        val serial = json.decodeFromString<SerialMessage>(response.bodyAsText())
        return CompletableDeferred(clientImpl.utils.createMessage(serial))
    }

    suspend fun deleteMessage(message : Message) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${message.channel.id}/messages/${message.id}") {
            method = HttpMethod.Delete
        }
    }

    suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode> {
        var rc = RedeemedCode()
        var la : Long
        val start = System.currentTimeMillis()
        try {
            clientImpl.utils.discordHTTPClient.request("$BASE_URL/entitlements/gift-codes/$code/redeem") {
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
                rc = RedeemedCode(
                    code,
                    la.absoluteValue,
                    RedeemedCodeStatusType.INVALID,
                    RedeemedCodeErrorType.UNKNOWN_CODE
                )
            }
        }
        return CompletableDeferred(rc)
    }

    suspend fun block(user : User) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/relationships/${user.id}") {
            method = HttpMethod.Put
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"type\":2}").toString())
        }
    }

    suspend fun editMessage(message : Message, edit : MessageOptions) : CompletableDeferred<Message> {
        val response =
            clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${message.channel.id}/messages/${message.id}") {
                method = HttpMethod.Patch
                headers {
                    append("Content-Type", "application/json")
                }
                setBody(json.encodeToString(edit))
            }
        val serial = json.decodeFromString<SerialMessage>(response.bodyAsText())
        return CompletableDeferred(clientImpl.utils.createMessage(serial))
    }

    fun convertIdToUnix(id : String) : Long {
        return if (id.isNotBlank()) {
            (id.toLong() / 4194304 + 1420070400000).absoluteValue
        } else {
            0
        }
    }

    fun muteGuild(guild : Guild, i : Int) {
        log("NOT IMPLEMENTED // TODO")
    }

    suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message? {
        return clientImpl.utils.fetchMessageById(id, channel)
    }

    suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : DiscordUser,
        filters : MessageSearchFilters,
    ) : Message? {
        return clientImpl.utils.fetchLastMessageInChannel(channel, user, filters)
    }
}
