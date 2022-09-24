package org.caffeine.chaos.api.payloads.gateway

import org.caffeine.chaos.api.payloads.gateway.data.SerialGuild
import org.caffeine.chaos.api.payloads.gateway.data.guild.create.GuildCreateD

@kotlinx.serialization.Serializable
data class GuildCreate(
    val d : GuildCreateD = GuildCreateD(),
    override val op : Int = 0,
    override val s : Int = 0,
    override val t : String = "",
) : BasePayload()
