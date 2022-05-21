package org.caffeine.chaos.api.client.message

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.client.DiscordUser

@Serializable
data class MessageMention(
    override val username: String,
    override val discriminator: String,
    override val id: String,
    override val avatar: String? = "",
) : DiscordUser(username, discriminator, id, avatar)