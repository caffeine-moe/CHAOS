package org.caffeine.chaos.api.client.connection.payloads.gateway.ready

import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateD

@kotlinx.serialization.Serializable
data class ReadyD(
    val country_code : String? = null,
    val user : ReadyDUser,
    val user_settings : ReadyDUserSettings? = null,
    val user_settings_proto : String? = null,
    val v : Int,
    val relationships : MutableList<ReadyDRelationship>? = null,
    val guilds : MutableList<GuildCreateD>,
    val private_channels : MutableList<ReadyDPrivateChannel>,
    val session_id : String,
)
