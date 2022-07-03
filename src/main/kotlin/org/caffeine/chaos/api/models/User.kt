package org.caffeine.chaos.api.models

import org.caffeine.chaos.api.models.interfaces.IDiscordUser
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class User(
    override val username : String = "",
    override val discriminator : String = "",
    override val avatar : String? = "",
    override val id : String = "",
) : IDiscordUser {
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
}