package org.caffeine.chaos.api.client.connection.payloads.gateway.user.ready

import org.caffeine.chaos.api.client.connection.payloads.gateway.guild.create.GuildCreateD

@kotlinx.serialization.Serializable
data class ReadyD(
    val country_code : String,
    val user : ReadyDUser,
    val user_settings : ReadyDUserSettings,
    val user_settings_proto : String,
    val v : Int,
    val relationships : MutableList<ReadyDRelationship>,
    val guilds : MutableList<GuildCreateD>,
    val private_channels : MutableList<ReadyDPrivateChannel>,
    val session_id : String,
)
