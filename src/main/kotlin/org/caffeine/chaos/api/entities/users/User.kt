package org.caffeine.chaos.api.entities.users

import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.DMChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageSearchFilters
import org.caffeine.chaos.api.typedefs.RelationshipType

interface User {
    val username : String
    val discriminator : String
    val discriminatedName : String
    val id : Snowflake
    val avatar : String?
    val bot : Boolean
    val relation : RelationshipType
    val asMention : String
    suspend fun fetchLastMessageInChannel(channel : TextBasedChannel, filters : MessageSearchFilters) : Message?
    fun avatarUrl() : String

    suspend fun openDM() : DMChannel?
}