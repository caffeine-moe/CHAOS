package org.caffeine.chaos.api.client.user

import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.client.DMCreateRequest
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialPrivateChannel
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.BaseChannel
import org.caffeine.chaos.api.entities.channels.DMChannel
import org.caffeine.chaos.api.entities.channels.GuildChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Emoji
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.guild.GuildMemberData
import org.caffeine.chaos.api.entities.guild.Role
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.*
import org.caffeine.chaos.api.utils.*
import kotlin.math.absoluteValue

data class ClientUserImpl(
    override var verified : Boolean,
    override var username : String,
    override var discriminator : String,
    override var id : Snowflake,
    override var email : String?,
    override var bio : String?,
    override var settings : ClientUserSettings?,
    override var avatar : String?,
    override var premium : Boolean?,
    override var token : String,
    override var relation : RelationshipType,
    override var bot : Boolean,
    override var client : ClientImpl,
) : ClientUser {

    override var relationships : HashMap<Snowflake, User> = HashMap()
    override val friends : Map<Snowflake, User> get() = relationships.filterValues { it.relation == RelationshipType.FRIEND }
    override val blocked : Map<Snowflake, User> get() = relationships.filterValues { it.relation == RelationshipType.BLOCKED }

    override var asMention : String = "<@${id}>"

    override val discriminatedName : String
        get() = "$username#$discriminator"

    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        filters : MessageSearchFilters,
    ) : Message? {
        return client.utils.fetchLastMessageInChannel(channel, this, filters)
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

    override var channels = HashMap<Snowflake, BaseChannel>()
    override var guilds = HashMap<Snowflake, Guild>()
    override var guildMembers = HashMap<Snowflake, GuildMemberData>()
    override val guildRoles = HashMap<Snowflake, Role>()
    override var emojis = HashMap<Snowflake, Emoji>()

    override val dmChannels : Map<Snowflake, DMChannel>
        get() = channels.values.filterIsInstance<DMChannel>().associateBy { it.id }

    override val textChannels : Map<Snowflake, TextBasedChannel>
        get() = channels.values.filterIsInstance<TextBasedChannel>().associateBy { it.id }

    override val guildChannels : Map<Snowflake, GuildChannel>
        get() = channels.values.filterIsInstance<GuildChannel>().associateBy { it.id }

    override val messageCache : HashMap<Snowflake, Message> = HashMap()

    override suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message> {
        return client.utils.fetchMessages(channel, filters)
    }

    override suspend fun fetchChannelFromId(id : Snowflake) : BaseChannel? {
        return this.channels[id]
    }

    override suspend fun replyMessage(message : Message, data : MessageData) : CompletableDeferred<Message> {
        val reply = MessageReply(
            data.content,
            data.tts,
            data.nonce,
            MessageReference(
                message.guild?.id.toString(),
                message.channel.id.toString(),
                message.id.toString()
            )
        )
        val jsondata = json.encodeToString(reply)
        val response =
            client.httpClient.post("$BASE_URL/channels/${message.channel.id}/messages", jsondata).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(client.utils.createMessage(serial))
    }

    override suspend fun deleteChannel(channel : BaseChannel) {
        client.httpClient.delete("$BASE_URL/channels/${channel.id}")
    }

    override fun unblock(user : User) {
        return
    }

    override fun removeFriend(friend : User) {
        return
    }

    override suspend fun setHouse(house : HypeSquadHouseType) {
        if (house == HypeSquadHouseType.NONE) {
            client.httpClient.delete("$BASE_URL/hypesquad/online")
        } else {
            val data = json.parseToJsonElement("{\"house_id\":${house.ordinal}}").toString()
            client.httpClient.post("$BASE_URL/hypesquad/online", data)
        }
    }

    override suspend fun setCustomStatus(status : CustomStatus) {
        val text = json.encodeToString(status)
        val data = "{ \"custom_status\" : $text }"
        client.httpClient.patch("$BASE_URL/users/@me/settings", data)
    }

    override suspend fun setTheme(theme : ThemeType) {
        val data = json.parseToJsonElement("{\"theme\":\"${theme.value}\"}").toString()
        client.httpClient.patch("$BASE_URL/users/@me/settings", data)
    }

    override suspend fun setStatus(status : StatusType) {
        val data = json.parseToJsonElement("{\"status\":\"${status.value}\"}").toString()
        client.httpClient.patch("$BASE_URL/users/@me/settings", data)
    }

    override suspend fun sendMessage(channel : BaseChannel, message : String) : CompletableDeferred<Message> =
        sendMessage(channel, MessageBuilder().append(message))


    override suspend fun sendMessage(
        channel : BaseChannel,
        message : MessageData,
    ) : CompletableDeferred<Message> {
        val data = json.encodeToString(message)
        val response = client.httpClient.post("$BASE_URL/channels/${channel.id}/messages", data).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(client.utils.createMessage(serial))
    }

    override suspend fun deleteMessage(message : Message) {
        client.httpClient.delete("$BASE_URL/channels/${message.channel.id}/messages/${message.id}").await()
    }

    override suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode> {
        var rc = RedeemedCode()
        var la : Long
        val start = System.currentTimeMillis()
        try {
            client.httpClient.post("$BASE_URL/entitlements/gift-codes/$code/redeem")
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
        client.httpClient.post("$BASE_URL/users/@me/relationships/${user.id}", data)
    }

    override suspend fun editMessage(
        message : Message,
        edit : MessageData,
    ) : CompletableDeferred<Message> {
        val data = json.encodeToString(edit)
        val response =
            client.httpClient.patch(
                "$BASE_URL/channels/${message.channel.id}/messages/${message.id}",
                data
            ).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(client.utils.createMessage(serial))
    }

    override fun muteGuild(guild : Guild, i : Int) {
        log("NOT IMPLEMENTED // TODO")
    }

    override suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message {
        return client.utils.fetchMessageById(id, channel)
    }

    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : User,
        filters : MessageSearchFilters,
    ) : Message? {
        return client.utils.fetchLastMessageInChannel(channel, user, filters)
    }

    override suspend fun fetchGuild(guildId : Snowflake) : Guild = client.utils.fetchGuild(guildId)

    override suspend fun fetchGuildMember(user : User, guild : Guild) : GuildMemberData? =
        client.utils.fetchGuildMemberData(user.id, guild)

    override suspend fun openDM() : DMChannel? = null

    override suspend fun openDMWith(id : Snowflake) : DMChannel {
        return dmChannels.values.firstOrNull { it.recipients.containsKey(id) && it.recipients.size == 1 }
            ?: kotlin.run {
                val response = client.httpClient.post(
                    "$BASE_URL/users/@me/channels",
                    json.encodeToString(
                        DMCreateRequest(id)
                    )
                ).await()
                val serial = json.decodeFromString<SerialPrivateChannel>(response)
                return client.utils.createDMChannel(serial)
            }
    }

}