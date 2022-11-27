package org.caffeine.chaos.api.client.connection.payloads.gateway.ready

import org.caffeine.chaos.api.client.connection.payloads.gateway.SerialUser

@kotlinx.serialization.Serializable
data class ReadyDPrivateChannel(
    val id : String = "",
    val last_message_id : String = "",
    val recipients : List<SerialUser> = emptyList(),
    val name : String = "",
    val type : Int = 0,
)