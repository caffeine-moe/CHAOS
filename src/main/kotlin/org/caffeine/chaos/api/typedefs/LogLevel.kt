package org.caffeine.chaos.api.typedefs

import org.caffeine.chaos.api.client.Client

data class LogLevel(val level : LoggerLevel, val client : Client)

enum class LoggerLevel {
    NONE,
    LOW,
    MEDIUM,
    ALL;
}