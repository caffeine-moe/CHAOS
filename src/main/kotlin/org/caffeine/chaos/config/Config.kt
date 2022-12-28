package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token : String,
    val prefix : String,
    val updater : Updater,
    val logger : Logger,
    val afk : AFK,
    val autoDelete : AutoDelete,
    val antiScam : AntiScam,
    val backup : Backup,
    val restore : Restore,
    val nitroSniper : NitroSniper,
    val autoBump : AutoBump,
    val cdnpls : CDNPls,
)