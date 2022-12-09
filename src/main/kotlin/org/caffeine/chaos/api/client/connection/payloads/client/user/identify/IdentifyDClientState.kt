package org.caffeine.chaos.api.client.connection.payloads.client.user.identify

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Serializable
data class IdentifyDClientState(
    val guild_hashes : JsonObject = Json.decodeFromString("{}"),
    val highest_last_message_id : String = "0",
    val read_state_version : Int = 0,
    val user_guild_settings_version : Int = -1,
    val user_settings_version : Int = -1,
    val private_channels_version : Int = 0,
    val api_code_version : Int = 0,
)
