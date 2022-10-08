package org.caffeine.chaos.api.payloads.gateway.data.ready

@kotlinx.serialization.Serializable
data class ReadyDUser(
    val avatar : String?,
    val bio : String,
    val desktop : Boolean,
    val discriminator : String,
    val email : String?,
    val flags : Int,
    val id : String,
    val mfa_enabled : Boolean,
    val mobile : Boolean,
    val nsfw_allowed : Boolean,
    val premium : Boolean,
    val purchased_flags : Int,
    val username : String,
    val verified : Boolean,
)
