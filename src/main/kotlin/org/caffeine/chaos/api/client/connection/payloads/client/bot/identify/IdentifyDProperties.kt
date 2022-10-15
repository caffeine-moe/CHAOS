package org.caffeine.chaos.api.client.connection.payloads.client.bot.identify

import kotlinx.serialization.Serializable

@Serializable
data class IdentifyDProperties(
    val os : String,
    val browser : String,
    val device : String
)
