package org.caffeine.chaos.api.client.message

import org.caffeine.chaos.api.client.DiscordUser

@kotlinx.serialization.Serializable
data class MessageAuthor(
    override val username: String,
    override val discriminator: String,
    override val id: String,
    override val avatar: String? = "",
) : DiscordUser() {
    override val discriminatedName = "$username#$discriminator"
}