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
import org.caffeine.chaos.api.entities.guild.GuildMember
import org.caffeine.chaos.api.entities.guild.Role
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.typedefs.RelationshipType
import org.caffeine.chaos.api.utils.MessageSendData

open class BaseClientUserImpl(
    override val bio : String?,
    override val token : String,
    override val client : Client,
    override val username : String,
    override val id : Snowflake,
    override val avatar : String?,
    override val bot : Boolean,
    override val relation : RelationshipType = RelationshipType.NONE,
) : BaseClientUser {

    override val asMention : String = "<@${id}>"

    override var channels = HashMap<Snowflake, BaseChannel>()
    override var guilds = HashMap<Snowflake, Guild>()
    override var guildMembers = HashMap<Snowflake, GuildMember>()
    override val guildRoles = HashMap<Snowflake, Role>()
    override var emojis = HashMap<Snowflake, Emoji>()
    override val messageCache : HashMap<Snowflake, Message> = HashMap()
    override suspend fun sendMessage(
        channel : TextBasedChannel,
        message : MessageSendData,
    ) : CompletableDeferred<Message> {
        return when (client) {
            is ClientUser -> client.user.sendMessage(channel, message)
            is BotClientUser -> client.user.sendMessage(channel, message)
            else -> (client.user as BotClientUser).sendMessage(channel, message)
        }
    }

    override val dmChannels : Map<Snowflake, DMChannel>
        get() = channels.values.filterIsInstance<DMChannel>().associateBy { it.id }

    override val textChannels : Map<Snowflake, TextBasedChannel>
        get() = channels.values.filterIsInstance<TextBasedChannel>().associateBy { it.id }

    override val guildChannels : Map<Snowflake, GuildChannel>
        get() = channels.values.filterIsInstance<GuildChannel>().associateBy { it.id }
}