package org.caffeine.chaos.api.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MuteForever(
    @SerialName("mute_config")
    val muteConfig : MuteConfig = MuteConfig(),
    @SerialName("muted")
    val muted : Boolean = false,
)

@Serializable
data class MuteConfig(
    @SerialName("end_time")
    val endTime : Long? = null,
    @SerialName("selected_time_window")
    val selectedTimeWindow : Int = 0,
)