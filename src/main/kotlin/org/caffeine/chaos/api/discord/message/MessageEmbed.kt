package org.caffeine.chaos.api.discord.message

@kotlinx.serialization.Serializable
data class MessageEmbed(
    val type : String = "",
    val url : String = "",
    val title : String = "",
    val description : String = "",
)