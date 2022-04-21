package org.caffeine.chaos.api.client.message

@kotlinx.serialization.Serializable
data class MessageMention(
    val username: String,
    val discriminator: String,
    val id: String,
    val avatar: String? = "",
)
