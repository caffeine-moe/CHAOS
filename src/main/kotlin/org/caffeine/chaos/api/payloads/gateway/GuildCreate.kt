package org.caffeine.chaos.api.payloads.gateway

import org.caffeine.chaos.api.payloads.gateway.data.SerialGuild

@kotlinx.serialization.Serializable
data class GuildCreate(
    val d : SerialGuild = SerialGuild(),
    override val op : Int = 0,
    override val s : Int = 0,
    override val t : String = "",
) : BasePayload()
