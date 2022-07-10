package org.caffeine.chaos.api.payloads.gateway.data.ready

@kotlinx.serialization.Serializable
data class ReadyD(
    val country_code : String,
    val user : ReadyDUser,
    val user_settings : ReadyDUserSettings,
    val user_settings_proto : String,
    val v : Int,
    //val relationships : MutableList<ClientRelationship>,
    val guilds : MutableList<ReadyDGuild>,
    val session_id : String,
)