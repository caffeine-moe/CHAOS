package org.caffeine.chaos.api.payloads.gateway.data.init

import kotlinx.serialization.Serializable

@Serializable
data class InitData(
    val heartbeat_interval : Long,
    val _trace : List<String>,
)