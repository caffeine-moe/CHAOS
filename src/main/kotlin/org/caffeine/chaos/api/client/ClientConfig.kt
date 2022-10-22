package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.StatusType

data class ClientConfig(
    val token : String,
    val clientType : ClientType,
    val statusType : StatusType,
    val logLevel : LogLevel
)