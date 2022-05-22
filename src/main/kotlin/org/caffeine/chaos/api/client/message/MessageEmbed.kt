package org.caffeine.chaos.api.client.message

@kotlinx.serialization.Serializable
data class MessageEmbed(
    val type: String = "",
    val url: String = "",
    val title: String = "",
    val description: String = "",
)