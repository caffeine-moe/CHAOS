package org.caffeine.chaos.api.client

import kotlinx.serialization.Serializable

@Serializable
data class ClientGuild(
    val name: String,
    val id: String
)
