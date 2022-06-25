package org.caffeine.chaos.api.payloads.client.data.resume

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.payloads.BaseData

@Serializable
data class ResumeD(
    val seq : Int = 0,
    val session_id : String = "",
    val token : String = "",
) : BaseData()