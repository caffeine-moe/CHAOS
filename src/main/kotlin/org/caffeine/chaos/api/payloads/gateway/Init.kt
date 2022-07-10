package org.caffeine.chaos.api.payloads.gateway

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.payloads.gateway.data.init.InitD

@Serializable
data class Init(
    override val t : String?,
    override val op : Int,
    val d : InitD,
) : BasePayload()