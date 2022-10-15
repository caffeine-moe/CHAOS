package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
open class BasePayload(
    @Transient open val op : Int = 0,
    @Transient open val s : Int? = 0,
    @Transient open val t : String? = "",
)
