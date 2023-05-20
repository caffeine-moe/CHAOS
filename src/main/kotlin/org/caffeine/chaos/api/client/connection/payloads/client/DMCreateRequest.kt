package org.caffeine.chaos.api.client.connection.payloads.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.entities.Snowflake

@Serializable
data class DMCreateRequest(
    @SerialName("recipient_id")
    val userId : Snowflake,
)