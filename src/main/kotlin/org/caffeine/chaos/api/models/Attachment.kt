package org.caffeine.chaos.api.models

data class Attachment(
    val content_type: String = "",
    val filename: String = "",
    val height: Int = 0,
    val id: String = "",
    val proxy_url: String = "",
    val size: Int = 0,
    val url: String = "",
    val width: Int = 0
)