package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Updater(
    val enabled : Boolean = false,
    val notify : Boolean = false,
    val autoDownload : Boolean = false,
    val exit : Boolean = false,
)