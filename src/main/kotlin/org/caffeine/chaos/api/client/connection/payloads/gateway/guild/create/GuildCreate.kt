package org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create

import SerialGuild
import org.caffeine.chaos.api.client.connection.payloads.gateway.BasePayload

@kotlinx.serialization.Serializable
data class GuildCreate(
    val d : SerialGuild = SerialGuild(),
    override val op : Int = 0,
    override val s : Int = 0,
    override val t : String = "",
) : BasePayload()
