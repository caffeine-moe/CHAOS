package org.caffeine.chaos.api.entities.users

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.DMChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.typedefs.RelationshipType

data class UserImpl(
    override val username : String = "",
    override val avatar : String? = "",
    override val id : Snowflake = Snowflake(0),
    override val relation : RelationshipType,
    override val bot : Boolean = false,
    override val client : Client,
) : User {

    override val asMention : String = "<@${id}>"
    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        filters : MessageSearchFilters,
    ) : Message? {
        return client.user.fetchLastMessageInChannel(channel, filters)
    }

    override suspend fun openDM() : DMChannel = client.user.openDMWith(id)

    override fun equals(other : Any?) : Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is User) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode() : Int {
        var result = username.hashCode()
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        result = 31 * result + relation.hashCode()
        result = 31 * result + bot.hashCode()
        result = 31 * result + client.hashCode()
        result = 31 * result + asMention.hashCode()
        return result
    }
}