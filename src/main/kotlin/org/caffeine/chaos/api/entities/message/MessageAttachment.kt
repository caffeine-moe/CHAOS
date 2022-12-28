package org.caffeine.chaos.api.entities.message

import kotlinx.serialization.Serializable

@Serializable
data class MessageAttachment(
    val contentType : String = "",
    val filename : String = "",
    val height : Int = 0,
    val id : String = "",
    val proxyUrl : String = "",
    val size : Int = 0,
    val url : String = "",
    val width : Int = 0,
)