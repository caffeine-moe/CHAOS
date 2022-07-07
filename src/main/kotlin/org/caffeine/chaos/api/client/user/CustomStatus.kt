package org.caffeine.chaos.api.client.user

data class CustomStatus(
    val emoji_id : String? = "",
    val emoji_name : String? = "",
    val expires_at : String? = "",
    val text : String = "",
)