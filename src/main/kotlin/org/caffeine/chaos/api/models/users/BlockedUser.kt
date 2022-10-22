package org.caffeine.chaos.api.models.users

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageSearchFilters
import kotlin.math.absoluteValue

data class BlockedUser(
    override val username : String = "",
    override val discriminator : String = "",
    override val avatar : String? = "",
    override val id : String = "",
    override val bot : Boolean,
    private val client : Client,
) : DiscordUser {

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
                "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/$id/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }

    fun unblock() {
        client.user.unblock(this)
    }
}
