package org.caffeine.chaos.api.client.connection.payloads.gateway.bot.ready

import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateD

@kotlinx.serialization.Serializable
data class ReadyD(
    val user : ReadyDUser,
    val v : Int,
    val guilds : MutableList<GuildCreateD>,
    val session_id : String,
)
