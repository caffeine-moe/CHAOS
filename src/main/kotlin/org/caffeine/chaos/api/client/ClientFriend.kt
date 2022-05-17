package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class ClientFriend(
    val username: String,
    val discriminator: String,
    val id: String,
    val avatar: String?,
)
