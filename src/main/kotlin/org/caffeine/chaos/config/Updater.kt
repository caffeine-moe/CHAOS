package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Updater(
    val auto_download: Boolean = false,
    val enabled: Boolean = false,
    val exit: Boolean = false,
    val notify: Boolean = false,
)