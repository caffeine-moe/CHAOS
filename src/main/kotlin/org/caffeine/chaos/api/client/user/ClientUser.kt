package org.caffeine.chaos.api.client.user

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
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
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.typedefs.ThemeType

interface ClientUser : DiscordUser {

    val verified : Boolean
    override val username : String
    override val discriminator : String
    override val id : String
    val email : String?
    val bio : String?
    val settings : ClientUserSettings?
    override val avatar : String?
    val relationships : ClientUserRelationships?
    val premium : Boolean?
    val token : String
    override val bot : Boolean
    val client : Client
    override val asMention : String

    val guilds : Map<String, Guild>
    val channels : Map<String, BaseChannel>
    val dmChannels : Map<String, DMChannel>

    suspend fun deleteChannel(channel : BaseChannel)

    fun unblock(user : BlockedUser)

    fun removeFriend(friend : Friend)

    suspend fun setHouse(house : HypeSquadHouseType)

    suspend fun setCustomStatus(status : String)
    suspend fun setTheme(theme : ThemeType)

    suspend fun setStatus(status : StatusType)

    suspend fun sendMessage(channel : BaseChannel, message : MessageOptions) : CompletableDeferred<Message>

    suspend fun deleteMessage(message : Message)

    suspend fun redeemCode(code : String) : CompletableDeferred<RedeemedCode>

    suspend fun block(user : User)

    suspend fun editMessage(message : Message, edit : MessageOptions) : CompletableDeferred<Message>

    fun convertIdToUnix(id : String) : Long

    fun muteGuild(guild : Guild, i : Int)

    suspend fun fetchMessageById(id : String, channel : TextBasedChannel) : Message?

    suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        user : DiscordUser,
        filters : MessageSearchFilters,
    ) : Message?

    suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message>

    suspend fun fetchChannelFromId(id : String) : BaseChannel?
}
