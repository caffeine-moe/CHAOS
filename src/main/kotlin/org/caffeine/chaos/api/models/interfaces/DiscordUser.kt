package org.caffeine.chaos.api.models.interfaces

import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageSearchFilters

interface DiscordUser {
    val username : String
    val discriminator : String
    val discriminatedName : String
    val id : String
    val avatar : String?
    val bot : Boolean
    val asMention : String
    suspend fun fetchLastMessageInChannel(channel : TextBasedChannel, filters : MessageSearchFilters) : Message?
    fun avatarUrl() : String
}
