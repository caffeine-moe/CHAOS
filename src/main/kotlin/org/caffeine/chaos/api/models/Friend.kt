package org.caffeine.chaos.api.models

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.channels.DMChannel
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions
import java.util.*
import kotlin.math.absoluteValue

data class Friend(
override val username : String = "",
override val discriminator : String = "",
override val avatar : String? = "",
override val id : String = "",
private val client : Client
) : DiscordUser {
    override val discriminatedName = "$username#$discriminator"
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
    fun removeFriend() {
        client.user.removeFriend(this)
    }
    suspend fun sendMessage(message : MessageOptions) {
        client.user.sendMessage(DMChannel(id, client, ChannelType.DM, "", Date(), username, 69), message)
    }
}