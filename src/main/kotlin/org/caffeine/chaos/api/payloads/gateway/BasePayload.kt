package org.caffeine.chaos.api.payloads.gateway

import kotlinx.serialization.Transient
import org.caffeine.chaos.api.payloads.BaseData

@kotlinx.serialization.Serializable
abstract class BasePayload(
    @Transient open val op : Int = 0,
    @Transient open val s : Int? = 0,
    @Transient open val t : String? = "",
    @Transient open val d : BaseData? = BaseData(),
)
