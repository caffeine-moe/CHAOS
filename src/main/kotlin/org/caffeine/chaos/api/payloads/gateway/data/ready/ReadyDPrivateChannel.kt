package org.caffeine.chaos.api.payloads.gateway.data.ready

@kotlinx.serialization.Serializable
data class ReadyDPrivateChannel(
    val id : String = "",
    val last_message_id : String = "",
    val recipients : List<Recipient> = listOf(),
    val name : String = "",
    val type : Int = 0,
)

@kotlinx.serialization.Serializable
data class Recipient(
    val avatar : String = "",
    val avatar_decoration : String? = null,
    val bot : Boolean = false,
    val discriminator : String = "",
    val id : String = "",
    val public_flags : Int = 0,
    val username : String = "",
)
