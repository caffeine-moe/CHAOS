package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class AntiScam(
    val enabled : Boolean,
    val block : Boolean,
)
