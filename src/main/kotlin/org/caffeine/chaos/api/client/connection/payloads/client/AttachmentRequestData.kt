package org.caffeine.chaos.api.client.connection.payloads.client

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentRequestData(
    val files : List<AttachmentSlotRequest>,
)