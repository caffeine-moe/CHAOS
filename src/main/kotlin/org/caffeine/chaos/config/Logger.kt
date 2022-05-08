package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Logger(
    val commands: Boolean,
    val responses: Boolean,
    val nitro_sniper: Boolean,
    val anti_scam: Boolean,
)
