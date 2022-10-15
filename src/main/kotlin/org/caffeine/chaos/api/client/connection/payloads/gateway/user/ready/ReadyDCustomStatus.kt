package org.caffeine.chaos.api.client.connection.payloads.gateway.user.ready

@kotlinx.serialization.Serializable
data class ReadyDCustomStatus(
    val emoji_id : String? = "",
    val emoji_name : String? = "",
    val expires_at : String? = "",
    val text : String = "",
)
