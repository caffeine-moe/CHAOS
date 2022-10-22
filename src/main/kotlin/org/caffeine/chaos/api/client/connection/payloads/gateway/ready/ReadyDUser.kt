package org.caffeine.chaos.api.client.connection.payloads.gateway.ready

@kotlinx.serialization.Serializable
data class ReadyDUser(
    val avatar : String?,
    val bio : String = "",
    val bot : Boolean = false,
    val desktop : Boolean? = null,
    val discriminator : String,
    val email : String?,
    val flags : Int,
    val id : String,
    val mfa_enabled : Boolean,
    val mobile : Boolean? = null,
    val nsfw_allowed : Boolean? = null,
    val premium : Boolean? = null,
    val purchased_flags : Int = 0,
    val username : String,
    val verified : Boolean, )
