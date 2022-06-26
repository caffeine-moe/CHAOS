package org.caffeine.chaos.api.discord.message

import org.caffeine.chaos.api.discord.client.DiscordUser

@kotlinx.serialization.Serializable
data class MessageAuthor(
    override val username : String,
    override val discriminator : String,
    override val id : String,
    override val avatar : String? = "",
) : DiscordUser() {
    override val discriminatedName = "$username#$discriminator"
}