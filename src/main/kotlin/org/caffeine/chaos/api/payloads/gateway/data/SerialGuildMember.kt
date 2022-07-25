package org.caffeine.chaos.api.payloads.gateway.data

@kotlinx.serialization.Serializable
data class SerialGuildMember(
    val avatar: String? = null,
    val communication_disabled_until: String? = null,
    val deaf: Boolean = false,
    val flags: Int = 0,
    val joined_at: String = "",
    val mute: Boolean = false,
    val nick: String = "",
    val pending: Boolean = false,
    val premium_since: String? = null,
    val roles: List<String> = listOf(),
    val user: SerialUser
)