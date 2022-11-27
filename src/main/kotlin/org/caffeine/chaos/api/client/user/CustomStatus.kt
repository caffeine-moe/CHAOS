package org.caffeine.chaos.api.client.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomStatus(
    val emoji_id : String? = null,
    val emoji_name : String? = null,
    val expires_at : String? = null,
    val text : String = "",
)
