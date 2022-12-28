package org.caffeine.chaos.api.client.connection.payloads.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DMCreateRequest(
    @SerialName("recipient_id")
    val userId : String,
)