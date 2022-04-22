package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class ClientChannelRecipient(
    val id: String,
    val username: String,
    val avatar: String?,
    val discriminator: String,
    val public_flags: Int,
    val bot: Boolean = false,
)
