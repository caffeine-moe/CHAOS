package org.caffeine.chaos.config

@kotlinx.serialization.Serializable
data class AFK (
    val message: String,
    val cooldown: Long,
    val status: String
    )