package org.caffeine.chaos.api.payloads.client.data.identify

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.utils.DiscordUtils

@Serializable
data class IdentifyD(
    val token : String,
    val properties : DiscordUtils.SuperProperties,
    val presence : IdentifyDPresence = IdentifyDPresence(),
    val compress : Boolean = false,
    val client_state : IdentifyDClientState = IdentifyDClientState(),
)