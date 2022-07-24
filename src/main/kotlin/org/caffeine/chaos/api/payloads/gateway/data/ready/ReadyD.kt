package org.caffeine.chaos.api.payloads.gateway.data.ready

import org.caffeine.chaos.api.payloads.gateway.data.SerialGuild

@kotlinx.serialization.Serializable
data class ReadyD(
    val country_code : String,
    val user : ReadyDUser,
    val user_settings : ReadyDUserSettings,
    val user_settings_proto : String,
    val v : Int,
    val relationships : MutableList<ReadyDRelationship>,
    val guilds : MutableList<SerialGuild>,
    val private_channels : MutableList<ReadyDPrivateChannel>,
    val session_id : String,
)