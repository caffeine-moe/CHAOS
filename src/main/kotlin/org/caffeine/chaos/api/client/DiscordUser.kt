package org.caffeine.chaos.api.client

import kotlinx.serialization.Serializable

@Serializable
data class DiscordUser(
    val username: String,
    val discriminator: Int,
    val id: String,
    val avatar: String?,
)
