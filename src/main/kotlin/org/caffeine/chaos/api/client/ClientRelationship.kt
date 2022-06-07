package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class ClientRelationship(
    val type : Int,
    val user : DefaultUser,
)