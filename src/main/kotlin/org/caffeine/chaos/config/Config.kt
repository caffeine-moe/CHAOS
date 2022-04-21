package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token: String,
    val prefix: String,
    val logger: Logger,
    val auto_delete: AutoDelete,
    val exchange: Exchange,
    val backup: Backup,
    val restore: Restore,
    val nitro_sniper: NitroSniper,
)
