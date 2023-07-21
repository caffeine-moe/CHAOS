package org.caffeine.chaos.api.client.connection.payloads.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentSlotRequest(
    val filename : String,
    @SerialName("file_size")
    val fileSize : Int,
    val id : String,
)