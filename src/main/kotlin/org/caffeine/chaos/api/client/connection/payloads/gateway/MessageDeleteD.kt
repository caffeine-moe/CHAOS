package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Serializable

@Serializable
class MessageDeleteD(
    val id : String,
)