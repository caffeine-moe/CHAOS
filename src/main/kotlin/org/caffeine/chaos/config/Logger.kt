package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Logger(
    val commands: Boolean,
    val responses: Boolean,
    val anti_scam: Boolean,
    val nitro_sniper: Boolean,
    val auto_bump: Boolean,
)
