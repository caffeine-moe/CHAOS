package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentSlot(
    val id : Int,
    @SerialName("upload_url")
    val uploadUrl : String,
    @SerialName("upload_filename")
    val uploadFilename : String,
)