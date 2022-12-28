package org.caffeine.chaos.api.client.connection.payloads.gateway

@kotlinx.serialization.Serializable
data class SerialEmoji(
    val animated : Boolean = false,
    val available : Boolean = false,
    val id : String = "",
    val managed : Boolean = false,
    val name : String = "",
    val require_colons : Boolean = false,
)