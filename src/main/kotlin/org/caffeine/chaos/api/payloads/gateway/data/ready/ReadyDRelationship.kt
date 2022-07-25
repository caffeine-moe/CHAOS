package org.caffeine.chaos.api.payloads.gateway.data.ready

import org.caffeine.chaos.api.payloads.gateway.data.SerialUser

@kotlinx.serialization.Serializable
data class ReadyDRelationship(
    val type : Int,
    val user : SerialUser,
)