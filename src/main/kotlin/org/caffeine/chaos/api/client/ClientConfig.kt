package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.typedefs.StatusType

interface ClientConfig {
    val token : String
    val clientType : ClientType
    val statusType : StatusType
    val loggerLevel : LoggerLevel
    val logLevel : LogLevel
}
