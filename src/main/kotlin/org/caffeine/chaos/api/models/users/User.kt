package org.caffeine.chaos.api.models.users

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import kotlin.math.absoluteValue

data class User(
    override val username : String = "",
    override val discriminator : String = "",
    override val avatar : String? = "",
    override val id : String = "",
    val client : Client
) : DiscordUser {

    override val avatarDecoration : String? = null
    override val banner : String? = null
    override val bannerColor : String? = null
    override val accentColour : String? = null

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