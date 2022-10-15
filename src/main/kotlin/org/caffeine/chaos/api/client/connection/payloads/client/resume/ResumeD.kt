package org.caffeine.chaos.api.client.connection.payloads.client.resume

import kotlinx.serialization.Serializable

@Serializable
data class ResumeD(
    val seq : Int = 0,
    val session_id : String = "",
    val token : String = "",
)
