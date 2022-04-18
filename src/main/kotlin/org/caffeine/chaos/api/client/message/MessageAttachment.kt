package org.caffeine.chaos.api.client.message

@kotlinx.serialization.Serializable
data class MessageAttachment(
    val id: String,
    val filename: String,
    val size: Int = 0,
    val url: String = "",
    val proxy_url: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val content_type: String = "",
)