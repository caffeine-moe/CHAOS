package org.caffeine.chaos.api.client.slashcommands

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendAppCommand(
    @SerialName("type")
    val type: Int = 0,
    @SerialName("application_id")
    val applicationId: String = "",
    @SerialName("guild_id")
    val guildId: String = "",
    @SerialName("channel_id")
    val channelId: String = "",
    @SerialName("session_id")
    val sessionId: String = "",
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("nonce")
    val nonce: String = "",
)