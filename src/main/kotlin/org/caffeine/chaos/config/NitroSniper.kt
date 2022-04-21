package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class NitroSniper(
    val enabled: Boolean,
    val silent: Boolean,
)
