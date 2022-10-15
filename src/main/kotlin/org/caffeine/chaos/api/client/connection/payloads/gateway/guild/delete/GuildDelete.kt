package org.caffeine.chaos.api.client.connection.payloads.gateway.guild.delete

import org.caffeine.chaos.api.client.connection.payloads.gateway.BasePayload

@kotlinx.serialization.Serializable
data class GuildDelete(
    val d : GuildDeleteD = GuildDeleteD(),
    override val op : Int = 0,
    override val s : Int = 0,
    override val t : String = "",
) : BasePayload()
