package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Serializable

@Serializable
data class AttachmentSlotResponse(
    val attachments : List<AttachmentSlot>,
)