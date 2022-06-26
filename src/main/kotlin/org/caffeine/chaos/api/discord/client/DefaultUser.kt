package org.caffeine.chaos.api.discord.client

@kotlinx.serialization.Serializable
data class DefaultUser(
    override val username : String,
    override val discriminator : String,
    override val avatar : String?,
    override val id : String,
) : DiscordUser()