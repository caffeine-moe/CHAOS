package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Serializable

@Serializable
data class SerialGuildChannel(
    val flags : Int = 0,
    override val id : String = "",
    override val name : String = "",
    val parent_id : String = "",
    val position : Int = 0,
    override val type : Int = 0,
    val topic : String = "",
) : SerialBaseChannel
