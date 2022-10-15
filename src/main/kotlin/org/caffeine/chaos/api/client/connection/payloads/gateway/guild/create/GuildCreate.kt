package org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create

import org.caffeine.chaos.api.client.connection.payloads.gateway.BasePayload

@kotlinx.serialization.Serializable
data class GuildCreate(
    val d : GuildCreateD = GuildCreateD(),
    override val op : Int = 0,
    override val s : Int = 0,
    override val t : String = "",
) : BasePayload()
