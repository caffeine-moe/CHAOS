package org.caffeine.chaos.api.client.connection.payloads.gateway

data class SerialTextBasedChannel(
    override val type : Int,
    override val id : String,
    override val name : String,
    val last_message_id : String,
) : SerialBaseChannel
