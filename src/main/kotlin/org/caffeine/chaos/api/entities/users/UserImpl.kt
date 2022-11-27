package org.caffeine.chaos.api.entities.users

import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.typedefs.RelationshipType
import kotlin.math.absoluteValue

data class UserImpl(
    override val username : String = "",
    override val discriminator : String = "",
    override val avatar : String? = "",
    override val id : Snowflake = Snowflake(""),
    override val relation : RelationshipType,
    override val bot : Boolean = false,
    val client : Client,
) : User {

    override val asMention : String = "<@${id}>"

    override val discriminatedName = "$username#$discriminator"
    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        filters : MessageSearchFilters,
    ) : Message? {
        return client.user.fetchLastMessageInChannel(channel, this, filters)
    }

    override fun avatarUrl() : String {
        return if (!avatar.isNullOrBlank()) {
            if (avatar.startsWith("a_")) {
                "https://cdn.discordapp.com/avatars/${id.asString()}/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/${id.asString()}/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }
}