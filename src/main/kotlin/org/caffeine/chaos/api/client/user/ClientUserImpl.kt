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
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
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
        clientImpl.httpClient.delete("$BASE_URL/channels/${channel.id}")
    }

    override fun unblock(user : BlockedUser) {
        return
    }

    override fun removeFriend(friend : Friend) {
        return
    }

    override suspend fun setHouse(house : HypeSquadHouseType) {
        if (house == HypeSquadHouseType.NONE) {
            clientImpl.httpClient.delete("$BASE_URL/hypesquad/online")
        } else {
            val data = json.parseToJsonElement("{\"house_id\":${house.ordinal}}").toString()
            clientImpl.httpClient.post("$BASE_URL/hypesquad/online", data)
        }
    }

    override suspend fun setCustomStatus(status : String) {
        val data = json.parseToJsonElement("{\"custom_status\":{\"text\":\"$status\"}}").toString()
        clientImpl.httpClient.patch("$BASE_URL/users/@me/settings", data)
    }

    override suspend fun setTheme(theme : ThemeType) {
        val data = json.parseToJsonElement("{\"theme\":\"${theme.value}\"}").toString()
        clientImpl.httpClient.patch("$BASE_URL/users/@me/settings", data)
    }

    override suspend fun setStatus(status : StatusType) {
        val data = json.parseToJsonElement("{\"status\":\"${status.value}\"}").toString()
        clientImpl.httpClient.patch("$BASE_URL/users/@me/settings", data)
    }

    override suspend fun sendMessage(channel : BaseChannel, message : MessageOptions) : CompletableDeferred<Message> {
        val data = json.encodeToString(message)
        val response = clientImpl.httpClient.post("$BASE_URL/channels/${channel.id}/messages", data).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(clientImpl.utils.createMessage(serial))
    }

    override suspend fun deleteMessage(message : Message) {
        clientImpl.httpClient.delete("$BASE_URL/channels/${message.channel.id}/messages/${message.id}")
    }

    override suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode> {
        var rc = RedeemedCode()
        var la : Long
        val start = System.currentTimeMillis()
        try {
            clientImpl.httpClient.post("$BASE_URL/entitlements/gift-codes/$code/redeem")
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
        val data = json.parseToJsonElement("{\"type\":2}").toString()
        clientImpl.httpClient.post("$BASE_URL/users/@me/relationships/${user.id}", data)
    }

    override suspend fun editMessage(message : Message, edit : MessageOptions) : CompletableDeferred<Message> {
        val data = json.encodeToString(edit)
        val response =
            clientImpl.httpClient.patch("$BASE_URL/channels/${message.channel.id}/messages/${message.id}", data).await()
        val serial = json.decodeFromString<SerialMessage>(response)
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
