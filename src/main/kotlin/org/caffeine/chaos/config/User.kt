package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val enabled : Boolean,
    val delay : Long,
)
