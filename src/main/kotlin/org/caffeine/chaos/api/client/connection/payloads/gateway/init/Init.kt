package org.caffeine.chaos.api.client.connection.payloads.gateway.init

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.client.connection.payloads.gateway.BasePayload

@Serializable
data class Init(
    override val t : String?,
    override val op : Int,
    val d : InitD,
) : BasePayload()
