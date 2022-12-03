package org.caffeine.chaos.api.client.connection.payloads.gateway

@kotlinx.serialization.Serializable
data class SerialPrivateChannel(
    override val id : String = "",
    val last_message_id : String = "",
    val recipients : List<SerialUser> = emptyList(),
    override val name : String = "",
    override val type : Int = 0,
) : SerialBaseChannel