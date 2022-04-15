package org.caffeine.chaos.api.client

import kotlinx.serialization.Serializable

@Serializable
data class ClientFriend(
    val user: DiscordUser
)
