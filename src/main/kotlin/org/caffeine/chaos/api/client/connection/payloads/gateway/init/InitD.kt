package org.caffeine.chaos.api.client.connection.payloads.gateway.init

import kotlinx.serialization.Serializable

@Serializable
data class InitD(
    val heartbeat_interval : Long,
    val _trace : List<String>,
)
