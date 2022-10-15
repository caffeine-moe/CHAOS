package org.caffeine.chaos.api.client.connection.payloads.gateway.bot.ready

@kotlinx.serialization.Serializable
data class ReadyDUser(
    val avatar : String?,
    val discriminator : String,
    val email : String?,
    val flags : Int,
    val id : String,
    val mfa_enabled : Boolean,
    val username : String,
    val verified : Boolean,
)
