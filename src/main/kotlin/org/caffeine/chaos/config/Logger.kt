package org.caffeine.chaos.config

import kotlinx.serialization.Serializable

@Serializable
data class Logger(
    val commands : Boolean,
    val responses : Boolean,
    val deletedMessages : Boolean,
    val antiScam : Boolean,
    val nitroSniper : Boolean,
    val autoBump : Boolean,
    val cdnpls : Boolean,
)