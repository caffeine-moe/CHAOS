package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token : String,
    val prefix : String,
    val updater : Updater,
    val logger : Logger,
    val web_ui : WebUI,
    val afk : AFK,
    val auto_delete : AutoDelete,
    val anti_scam : AntiScam,
    val exchange : Exchange,
    val backup : Backup,
    val restore : Restore,
    val nitro_sniper : NitroSniper,
    val auto_bump : AutoBump,
)
