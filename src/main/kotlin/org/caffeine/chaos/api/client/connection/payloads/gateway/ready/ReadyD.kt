package org.caffeine.chaos.api.client.connection.payloads.gateway.ready

import SerialGuild

@kotlinx.serialization.Serializable
data class ReadyD(
    val country_code : String = "",
    val user : ReadyDUser,
    val user_settings : ReadyDUserSettings = ReadyDUserSettings(),
    val user_settings_proto : String = "",
    val v : Int,
    val relationships : List<ReadyDRelationship> = emptyList(),
    val guilds : MutableList<SerialGuild>,
    val private_channels : MutableList<ReadyDPrivateChannel>,
    val session_id : String,
    val resume_gateway_url : String
)
