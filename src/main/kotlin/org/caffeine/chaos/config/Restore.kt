package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Restore(
    val dmed_users : Boolean,
    val friends : Boolean,
    val block_list : Boolean,
)
