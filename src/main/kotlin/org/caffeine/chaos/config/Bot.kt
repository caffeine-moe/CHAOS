package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Bot(
    val enabled: Boolean,
    val content_generation: Boolean,
    val delay: Long,
)
