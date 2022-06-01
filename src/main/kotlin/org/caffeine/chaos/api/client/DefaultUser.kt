package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class DefaultUser(
    override val username: String,
    override val discriminator: String,
    override val avatar: String?,
    override val id: String,
) : DiscordUser()