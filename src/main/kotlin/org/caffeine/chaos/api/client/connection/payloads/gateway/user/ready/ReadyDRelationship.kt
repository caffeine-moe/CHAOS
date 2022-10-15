package org.caffeine.chaos.api.client.connection.payloads.gateway.user.ready

import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialUser

@kotlinx.serialization.Serializable
data class ReadyDRelationship(
    val type : Int,
    val user : SerialUser,
)
