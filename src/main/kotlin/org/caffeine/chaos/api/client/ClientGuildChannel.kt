package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.client.message.MessageChannel

@kotlinx.serialization.Serializable
data class ClientGuildChannel(
    val flags : Int = 0,
    override var id : String = "",
    val last_message_id : String = "",
    val last_pin_timestamp : String = "",
    val name : String = "",
    val nsfw : Boolean = false,
    val parent_id : String = "",
    val position : Int = 0,
    val rate_limit_per_user : Int = 0,
    val topic : String = "",
    val type : Int = 0,
) : MessageChannel()