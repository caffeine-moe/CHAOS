package org.caffeine.chaos.api.client.connection.payloads.client.bot.identify

import kotlinx.serialization.Serializable

@Serializable
data class IdentifyD(
    val token : String,
    val intents : Int,
    val properties : IdentifyDProperties,
)
