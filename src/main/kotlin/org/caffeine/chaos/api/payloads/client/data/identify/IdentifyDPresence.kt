package org.caffeine.chaos.api.payloads.client.data.identify

import kotlinx.serialization.Serializable

@Serializable
data class IdentifyDPresence(
    val status : String = "online",
    val since : Int = 0,
    val activities : Array<String> = emptyArray(),
    val afk : Boolean = false,
)
