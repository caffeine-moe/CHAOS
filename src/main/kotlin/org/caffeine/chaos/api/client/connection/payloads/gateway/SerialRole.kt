package org.caffeine.chaos.api.client.connection.payloads.gateway

@kotlinx.serialization.Serializable
data class SerialRole(
    val id : String,
    val name : String,
    val color : Int,
    val hoist : Boolean,
    val icon : String?,
    val unicode_emoji : String?,
    val position : Int,
    val permissions : Long,
    val managed : Boolean,
    val mentionable : Boolean,
)