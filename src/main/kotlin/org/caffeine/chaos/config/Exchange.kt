package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Exchange(
    val base : String,
    val target : String,
)
