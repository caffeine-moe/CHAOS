package org.caffeine.chaos.api.discord.message

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.discord.client.DiscordUser

@Serializable
data class MessageMention(
    override val username : String,
    override val discriminator : String,
    override val id : String,
    override val avatar : String? = "",
) : DiscordUser() {
    override val discriminatedName = "$username#$discriminator"
}