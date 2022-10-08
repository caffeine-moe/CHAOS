package org.caffeine.chaos.api.payloads.gateway

import org.caffeine.chaos.api.payloads.gateway.data.ready.ReadyD

@kotlinx.serialization.Serializable
data class Ready(
    val d : ReadyD,
    override val op : Int,
    override val s : Int,
    override val t : String,
) : BasePayload()
