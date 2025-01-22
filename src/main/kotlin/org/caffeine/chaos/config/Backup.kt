package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val servers : Boolean,
    val dmedUsers : Boolean,
    val friends : Boolean,
    val blockList : Boolean,
)