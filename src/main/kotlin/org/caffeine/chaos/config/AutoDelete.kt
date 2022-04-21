package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class AutoDelete(
    val user: User,
    val bot: Bot,
)
