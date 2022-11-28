package org.caffeine.chaos.api.client.user

import arrow.core.Either
import arrow.core.Invalid
import arrow.core.Valid
import arrow.core.left
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialMessage
import org.caffeine.chaos.api.entities.channels.BaseChannel
import org.caffeine.chaos.api.entities.channels.DMChannel
import org.caffeine.chaos.api.entities.channels.GuildChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.*
import org.caffeine.chaos.api.utils.MessageData
import org.caffeine.chaos.api.utils.MessageReference
import org.caffeine.chaos.api.utils.MessageReply
import org.caffeine.chaos.api.utils.log
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
    override var client : Client,
    var clientImpl : ClientImpl,
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

    override var channels = HashMap<Snowflake, BaseChannel>()
    override var guilds = HashMap<Snowflake, Guild>()
    override val dmChannels : Map<Snowflake, DMChannel>
        get() = channels.values.filterIsInstance<DMChannel>().associateBy { it.id }

    override val textChannels : Map<Snowflake, TextBasedChannel>
        get() = channels.values.filterIsInstance<TextBasedChannel>().associateBy { it.id }

    override val guildChannels : Map<Snowflake, GuildChannel>
        get() = channels.values.filterIsInstance<GuildChannel>().associateBy { it.id }

    override suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message> {
        return clientImpl.utils.fetchMessages(channel, filters)
    }

    override suspend fun fetchChannelFromId(id : Snowflake) : BaseChannel? {
        return this.channels[id]
    }

    override suspend fun replyMessage(message : Message, messageData : MessageData) : CompletableDeferred<Either<String, Message>> {
        val reply = MessageReply(
            messageData.content,
            messageData.tts,
            messageData.nonce,
            MessageReference(
                message.guild?.id?.asString(),
                message.channel.id.asString(),
                message.id.asString()
            )
        )
        val data = json.encodeToString(reply)
        val response = clientImpl.httpClient.post("$BASE_URL/channels/${message.channel.id.asString()}/messages", data).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return when (val result = clientImpl.utils.createMessage(serial)) {
            is Invalid -> {
                val err = "Error in messageCreate: ${result.left()}"
                log(err, "API:", LogLevel(LoggerLevel.LOW, client))
                CompletableDeferred(Either.Left(err))
            }

            is Valid -> {
                CompletableDeferred(Either.Right(result.value))
            }
        }
    }

    /*    fun getGuild(channel : MessageChannel) : ClientGuild? {
            var guild : ClientGuild? = null
            for (g in client.autoDeleteUser.guilds) {
                for (c in g.channels) {
                    if (c.id == channel.id) {
                        guild = g
                    }
                }
            }
            return guild
        }*/

    override suspend fun deleteChannel(channel : BaseChannel) {
        clientImpl.httpClient.delete("$BASE_URL/channels/${channel.id.asString()}")
    }

    override fun unblock(user : User) {
        return
    }

    override fun removeFriend(friend : User) {
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

    override suspend fun setCustomStatus(status : CustomStatus) {
        val text = json.encodeToString(status)
        val data = "{ \"custom_status\" : $text }"
        println(data)
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

    override suspend fun sendMessage(
        channel : BaseChannel,
        message : MessageData,
    ) : CompletableDeferred<Either<String, Message>> {
        val data = json.encodeToString(message)
        val response = clientImpl.httpClient.post("$BASE_URL/channels/${channel.id.asString()}/messages", data).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return when (val result = clientImpl.utils.createMessage(serial)) {
            is Invalid -> {
                val err = "Error in messageCreate: ${result.left()}"
                log(err, "API:", LogLevel(LoggerLevel.LOW, client))
                CompletableDeferred(Either.Left(err))
            }

            is Valid -> {
                CompletableDeferred(Either.Right(result.value))
            }
        }
    }

    override suspend fun deleteMessage(message : Message) {
        clientImpl.httpClient.delete("$BASE_URL/channels/${message.channel.id.asString()}/messages/${message.id.asString()}")
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

    override suspend fun editMessage(
        message : Message,
        edit : MessageData,
    ) : CompletableDeferred<Either<String, Message>> {
        val data = json.encodeToString(edit)
        val response =
            clientImpl.httpClient.patch(
                "$BASE_URL/channels/${message.channel.id.asString()}/messages/${message.id.asString()}",
                data
            ).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return when (val result = clientImpl.utils.createMessage(serial)) {
            is Invalid -> {
                log("Error in messageCreate: ${result.left()}", level = LogLevel(LoggerLevel.LOW, client))
                CompletableDeferred(Either.Left(result.value))
            }

            is Valid -> {
                CompletableDeferred(Either.Right(result.value))
            }
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
        user : User,
        filters : MessageSearchFilters,
    ) : Message? {
        return clientImpl.utils.fetchLastMessageInChannel(channel, user, filters)
    }
}
