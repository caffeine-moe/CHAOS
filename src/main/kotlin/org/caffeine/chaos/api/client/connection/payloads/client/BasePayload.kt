package org.caffeine.chaos.api.client.connection.payloads.client

import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
open class BasePayload(
    @Transient open val op : Int = 0,
)
