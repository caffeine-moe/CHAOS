package org.caffeine.chaos.api.client.config

import org.caffeine.chaos.api.typedefs.APIVersion
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.typedefs.StatusType

data class ClientConfig(
    var token : String = "",
    var clientType : ClientType = ClientType.BOT,
    var statusType : StatusType = StatusType.ONLINE,
    var loggerLevel : LoggerLevel = LoggerLevel.NONE,
    var gatewayVersion : APIVersion = APIVersion.V9,
)