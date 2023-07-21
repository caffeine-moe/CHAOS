package org.caffeine.chaos.api.entities.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartialAttachment(
    val id : String,
    val filename : String,
    @SerialName("uploaded_filename")
    val uploadedFilename : String,
)