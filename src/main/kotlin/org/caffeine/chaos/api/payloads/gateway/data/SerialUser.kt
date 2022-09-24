package org.caffeine.chaos.api.payloads.gateway.data

@kotlinx.serialization.Serializable
data class SerialUser(
    val accent_color : Int = 0,
    val avatar : String = "",
    val avatar_decoration : String? = null,
    val banner : String? = null,
    val banner_color : String = "",
    val discriminator : String = "",
    val id : String = "",
    val bot : Boolean = false,
    val username : String = "",
)