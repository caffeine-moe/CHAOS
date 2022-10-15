package org.caffeine.chaos.api.client.connection.payloads.client.user.identify

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Serializable
data class IdentifyDClientState(
    @Contextual
    val guild_hashes : JsonObject = Json.decodeFromString("{}"),
    val highest_last_message_id : String = "0",
    val read_state_version : Int = 0,
    val user_guild_settings_version : Int = -1,
)
