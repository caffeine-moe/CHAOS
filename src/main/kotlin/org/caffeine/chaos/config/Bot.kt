package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Bot(
    val enabled : Boolean,
    val contentGeneration : Boolean,
    val delay : Long,
)