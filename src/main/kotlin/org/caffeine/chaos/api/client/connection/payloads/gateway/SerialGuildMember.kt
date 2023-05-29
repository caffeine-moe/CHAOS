package org.caffeine.chaos.api.client.connection.payloads.gateway

import org.caffeine.chaos.api.entities.Snowflake

@kotlinx.serialization.Serializable
data class SerialGuildMember(
    val avatar : String? = null,
    val communication_disabled_until : String? = null,
    val deaf : Boolean = false,
    val flags : Int = 0,
    val joined_at : String = "",
    val mute : Boolean = false,
    val nick : String = "",
    val pending : Boolean = false,
    val premium_since : String? = null,
    val roles : List<Snowflake> = listOf(),
    val user : SerialUser,
)