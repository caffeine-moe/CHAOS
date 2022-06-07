package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Backup(
    val servers : Boolean,
    val dmed_users : Boolean,
    val friends : Boolean,
    val block_list : Boolean,
)
