package org.caffeine.chaos.api.payloads.gateway

import org.caffeine.chaos.api.payloads.gateway.data.guild.delete.GuildDeleteD

@kotlinx.serialization.Serializable
data class GuildDelete(
    val d : GuildDeleteD = GuildDeleteD(),
    override val op : Int = 0,
    override val s : Int = 0,
    override val t : String = "",
) : BasePayload()