package org.caffeine.chaos.api.entities.users

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.DMChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.typedefs.RelationshipType
import org.caffeine.chaos.api.utils.CDN_URL

interface User {
    val client : Client
    val username : String
    val id : Snowflake
    val avatar : String?
    val bot : Boolean
    val relation : RelationshipType
    val asMention : String
    suspend fun fetchLastMessageInChannel(channel : TextBasedChannel, filters : MessageSearchFilters) : Message?
    fun avatarUrl() : String {
        val bit = if (!avatar.isNullOrBlank())
            if (avatar!!.startsWith("a_"))
                "avatars/$id/$avatar.gif"
            else
                "avatars/$id/$avatar.png"
        else
            "embed/avatars/${(id.value shr 22) % 6u}.png"

        return "$CDN_URL$bit"
    }

    suspend fun openDM() : DMChannel? = client.user.openDMWith(id)
}