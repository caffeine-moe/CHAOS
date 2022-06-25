package org.caffeine.chaos.api.payloads.client

import kotlinx.serialization.Transient
import org.caffeine.chaos.api.payloads.BaseData

@kotlinx.serialization.Serializable
open class BasePayload(
    @Transient open val op : Int = 0,
    @Transient open val d : BaseData = BaseData(),
)