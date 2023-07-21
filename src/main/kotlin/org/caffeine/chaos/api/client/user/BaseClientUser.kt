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
import org.caffeine.chaos.api.entities.guild.GuildMember
import org.caffeine.chaos.api.entities.guild.Role
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.utils.*

interface BaseClientUser : User {

    val bio : String?
    val token : String

    private val clientImpl : ClientImpl
        get() = client as ClientImpl

    val guilds : Map<Snowflake, Guild>
    val guildMembers : Map<Snowflake, GuildMember>
    val guildRoles : Map<Snowflake, Role>
    val emojis : Map<Snowflake, Emoji>
    val channels : Map<Snowflake, BaseChannel>
    val guildChannels : Map<Snowflake, GuildChannel>
    val textChannels : Map<Snowflake, TextBasedChannel>
    val dmChannels : Map<Snowflake, DMChannel>
    val messageCache : Map<Snowflake, Message>

    suspend fun sendMessage(channel : TextBasedChannel, message : MessageSendData) : CompletableDeferred<Message>
    suspend fun sendMessage(channel : TextBasedChannel, message : String) : CompletableDeferred<Message> =
        sendMessage(channel, MessageBuilder().append(message))

    suspend fun editMessage(message : Message, edit : MessageSendData) : CompletableDeferred<Message> {
        /*        if (client.user is BotClientUser)
                    return (client.user as BotClientUserImpl).sendMultiPartMessage(channel, message)*/
        (client.user as ClientUserImpl).finalizeAttachments(message.channel, edit)
        val data = jsonNoDefaults.encodeToString(edit)
        val response =
            clientImpl.httpClient.patch(
                "$BASE_URL/channels/${message.channel.id}/messages/${message.id}",
                data
            ).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(clientImpl.utils.createMessage(serial))
    }

    suspend fun deleteMessage(message : Message) =
        clientImpl.httpClient.delete("$BASE_URL/channels/${message.channel.id}/messages/${message.id}").await()

    suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message =
        clientImpl.utils.fetchMessageById(id, channel)

    suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message> = clientImpl.utils.fetchMessages(channel, filters)

    suspend fun fetchGuildMember(user : User, guild : Guild) : GuildMember =
        clientImpl.utils.fetchGuildMember(user.id, guild)

    suspend fun fetchChannelFromId(id : Snowflake) : BaseChannel? = this.channels[id]

    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        filters : MessageSearchFilters,
    ) : Message? {
        return clientImpl.utils.fetchLastMessageInChannel(channel, this, filters)
    }

    suspend fun replyMessage(message : Message, data : MessageData) : CompletableDeferred<Message> {
        val reply = clientImpl.utils.createReply(message, data)
        val jsondata = json.encodeToString(reply)
        val response =
            clientImpl.httpClient.post("$BASE_URL/channels/${message.channel.id}/messages", jsondata).await()
        val serial = json.decodeFromString<SerialMessage>(response)
        return CompletableDeferred(clientImpl.utils.createMessage(serial))
    }

    suspend fun deleteChannel(channel : BaseChannel) {
        clientImpl.httpClient.delete("$BASE_URL/channels/${channel.id}")
    }

    suspend fun fetchGuild(guildId : Snowflake) : Guild = clientImpl.utils.fetchGuild(guildId)

    suspend fun openDMWith(id : Snowflake) : DMChannel {
        return dmChannels.values.firstOrNull { it.recipients.containsKey(id) && it.recipients.size == 1 }
            ?: kotlin.run {
                val response = clientImpl.httpClient.post(
                    "$BASE_URL/users/@me/channels",
                    json.encodeToString(
                        DMCreateRequest(id)
                    )
                ).await()
                val serial = json.decodeFromString<SerialPrivateChannel>(response)
                return clientImpl.utils.createDMChannel(serial)
            }
    }

    suspend fun fetchUser(userId : Snowflake) : User = clientImpl.utils.fetchUser(userId)
}