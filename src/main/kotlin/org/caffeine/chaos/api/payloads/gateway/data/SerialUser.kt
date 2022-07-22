package org.caffeine.chaos.api.payloads.gateway.data

@kotlinx.serialization.Serializable
data class SerialUser (
    val username : String,
    val discriminator : String,
    val id : String,
    val avatar : String?,
)