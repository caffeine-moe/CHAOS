package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Restore(
    val dmedUsers : Boolean,
    val friends : Boolean,
    val blockList : Boolean,
)