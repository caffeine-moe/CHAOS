package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.models.channels.DMChannel
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters

interface BaseClientUser : DiscordUser {
    val verified : Boolean
    override val username : String
    override val discriminator : String
    override val id : String
    val email : String?
    val bio : String?
    val settings : ClientUserSettings
    override val avatar : String?
    val relationships : ClientUserRelationships
    val channels : Map<String, BaseChannel>
    val premium : Boolean
    val token : String
    val client : Client
    val clientImpl : ClientImpl
    val guilds : Map<String, Guild>

    suspend fun fetchMessagesFromChannel(channel : TextBasedChannel, filters : MessageFilters) : List<Message>
    suspend fun fetchChannelFromId(id : String) : BaseChannel?
}