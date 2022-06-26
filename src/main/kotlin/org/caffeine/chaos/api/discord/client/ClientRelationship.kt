package org.caffeine.chaos.api.discord.client

@kotlinx.serialization.Serializable
data class ClientRelationship(
    val type : Int,
    val user : DefaultUser,
)