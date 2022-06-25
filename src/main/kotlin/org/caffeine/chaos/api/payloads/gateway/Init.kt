package org.caffeine.chaos.api.payloads.gateway

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.payloads.gateway.data.init.InitData

@Serializable
data class Init(
    override val t : String?,
    override val op : Int,
    val d : InitData,
) : BasePayload()