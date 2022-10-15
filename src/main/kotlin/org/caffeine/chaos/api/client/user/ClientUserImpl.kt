package org.caffeine.chaos.api.client.user

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.models.channels.DMChannel
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.models.message.MessageSearchFilters
import org.caffeine.chaos.api.models.users.BlockedUser
import org.caffeine.chaos.api.models.users.Friend
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
import org.caffeine.chaos.api.typedefs.*
import org.caffeine.chaos.api.utils.log
import kotlin.math.absoluteValue

data class ClientUserImpl(
    override var verified : Boolean,
    override var username : String,
    override var discriminator : String,
    override var id : String,
    override var email : String?,
    override var bio : String?,
    override var settings : ClientUserSettings?,
    override var avatar : String?,
    override var relationships : ClientUserRelationships?,
    override var premium : Boolean?,
    override var token : String,
    override var bot : Boolean,
    override var client : Client,
    var clientImpl : ClientImpl,
) : ClientUser {

    var user : ClientUser = this

    override val discriminatedName : String
        get() = "$username#$discriminator"

    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        filters : MessageSearchFilters,
    ) : Message? {
        return clientImpl.utils.fetchLastMessageInChannel(channel, this, filters)
    }

    override fun avatarUrl() : String {
        return if (!avatar.isNullOrBlank()) {
            if (avatar!!.startsWith("a_")) {
                "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/$id/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }

    override var channels = HashMap<String, BaseChannel>()

    override var guilds = HashMap<String, Guild>()

    override suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message> {
        return clientImpl.utils.fetchMessages(channel, filters)
    }

    override suspend fun fetchChannelFromId(id : String) : BaseChannel? {
        return this.channels[id]
    }
    override val dmChannels : Map<String, DMChannel>
        get() = channels.values.filterIsInstance<DMChannel>().associateBy { it.id }

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

    override suspend fun deleteChannel(channel : BaseChannel) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${channel.id}") {
            method = HttpMethod.Delete
        }
    }

    override fun unblock(user : BlockedUser) {
        return
    }

    override fun removeFriend(friend : Friend) {
        return
    }

    override suspend fun setHouse(house : HypeSquadHouseType) {
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

    override suspend fun setCustomStatus(status : String) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"custom_status\":{\"text\":\"$status\"}}").toString())
        }
    }

    override suspend fun setTheme(theme : ThemeType) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"theme\":\"${theme.value}\"}").toString())
        }
    }

    override suspend fun setStatus(status : StatusType) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/settings") {
            method = HttpMethod.Patch
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"status\":\"${status.value}\"}").toString())
        }
    }

    override suspend fun sendMessage(channel : BaseChannel, message : MessageOptions) : CompletableDeferred<Message> {
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

    override suspend fun deleteMessage(message : Message) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/channels/${message.channel.id}/messages/${message.id}") {
            method = HttpMethod.Delete
        }
    }

    override suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode> {
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

    override suspend fun block(user : User) {
        clientImpl.utils.discordHTTPClient.request("$BASE_URL/users/@me/relationships/${user.id}") {
            method = HttpMethod.Put
            headers {
                append("Content-Type", "application/json")
            }
            setBody(json.parseToJsonElement("{\"type\":2}").toString())
        }
    }

    override suspend fun editMessage(message : Message, edit : MessageOptions) : CompletableDeferred<Message> {
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

    override fun convertIdToUnix(id : String) : Long {
        return if (id.isNotBlank()) {
            (id.toLong() / 4194304 + 1420070400000).absoluteValue
        } else {
            0
        }
    }

    override fun muteGuild(guild : Guild, i : Int) {
        log("NOT IMPLEMENTED // TODO")
    }

    override suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message? {
        return clientImpl.utils.fetchMessageById(id, channel)
    }

    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : DiscordUser,
        filters : MessageSearchFilters,
    ) : Message? {
        return clientImpl.utils.fetchLastMessageInChannel(channel, user, filters)
    }
}
