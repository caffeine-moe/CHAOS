package org.caffeine.chaos.config

@kotlinx.serialization.Serializable
data class AutoBump(
    val interval : Int,
    val error : List<Int>,
)