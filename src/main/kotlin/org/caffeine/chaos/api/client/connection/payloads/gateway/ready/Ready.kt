package org.caffeine.chaos.api.client.connection.payloads.gateway.ready

import org.caffeine.chaos.api.client.connection.payloads.gateway.BasePayload

@kotlinx.serialization.Serializable
data class Ready(
    val d : ReadyD,
    override val op : Int,
    override val s : Int,
    override val t : String,
) : BasePayload()
