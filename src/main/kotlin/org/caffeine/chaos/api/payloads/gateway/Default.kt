package org.caffeine.chaos.api.payloads.gateway

import org.caffeine.chaos.api.payloads.BaseData

@kotlinx.serialization.Serializable
class Default(
    val op : Int = 0,
    val s : Int? = 0,
    val t : String? = "",
    val d : BaseData? = BaseData(),
)