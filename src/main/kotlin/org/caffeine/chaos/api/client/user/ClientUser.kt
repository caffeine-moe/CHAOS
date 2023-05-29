package org.caffeine.chaos.api.client.user

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
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
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.typedefs.ThemeType
import org.caffeine.chaos.api.utils.MessageData

interface ClientUser : User {

    val verified : Boolean
    val email : String?
    val bio : String?
    val settings : ClientUserSettings?
    val premium : Boolean?
    val token : String

    val relationships : Map<Snowflake, User>
    val friends : Map<Snowflake, User>
    val blocked : Map<Snowflake, User>
    val guilds : Map<Snowflake, Guild>
    val guildMembers : Map<Snowflake, GuildMemberData>
    val guildRoles : Map<Snowflake, Role>
    val emojis : Map<Snowflake, Emoji>
    val channels : Map<Snowflake, BaseChannel>
    val guildChannels : Map<Snowflake, GuildChannel>
    val textChannels : Map<Snowflake, TextBasedChannel>
    val dmChannels : Map<Snowflake, DMChannel>

    val messageCache : Map<Snowflake, Message>

    suspend fun deleteChannel(channel : BaseChannel)

    fun unblock(user : User)

    fun removeFriend(friend : User)

    suspend fun setHouse(house : HypeSquadHouseType)

    suspend fun setCustomStatus(status : CustomStatus)
    suspend fun setTheme(theme : ThemeType)

    suspend fun setStatus(status : StatusType)

    suspend fun sendMessage(channel : BaseChannel, message : MessageData) : CompletableDeferred<Message>
    suspend fun sendMessage(channel : BaseChannel, message : String) : CompletableDeferred<Message>

    suspend fun deleteMessage(message : Message)

    suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode>

    suspend fun block(user : User)

    suspend fun editMessage(message : Message, edit : MessageData) : CompletableDeferred<Message>

    fun muteGuild(guild : Guild, i : Int)

    suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message?

    suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : User,
        filters : MessageSearchFilters,
    ) : Message?

    suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message>

    suspend fun fetchGuildMember(user : User, guild : Guild) : GuildMemberData
    suspend fun fetchChannelFromId(id : Snowflake) : BaseChannel?
    suspend fun replyMessage(message : Message, data : MessageData) : CompletableDeferred<Message>
    suspend fun fetchGuild(guildId : Snowflake) : Guild

    suspend fun openDMWith(id : Snowflake) : DMChannel?
}