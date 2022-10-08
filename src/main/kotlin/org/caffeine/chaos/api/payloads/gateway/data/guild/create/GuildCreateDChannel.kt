package org.caffeine.chaos.api.payloads.gateway.data.guild.create

import kotlinx.serialization.Serializable

@Serializable
data class GuildCreateDChannel(
    val flags : Int = 0,
    val id : String = "",
    val name : String = "",
    val parent_id : String? = null,
    val position : Int = 0,
    val type : Int = 0,
)
