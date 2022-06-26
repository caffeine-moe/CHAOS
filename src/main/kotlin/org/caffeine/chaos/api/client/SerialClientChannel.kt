package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class SerialClientChannel(
    val id : String,
    val type : Int,
    val last_message_id : String?,
    var recipients : List<ClientChannelRecipient>,
    var name : String? = null,
    var icon : String? = null,
    var owner_id : String? = null,
)