package org.caffeine.chaos.api.payloads.client

import kotlinx.serialization.Serializable

@Serializable
data class HeartBeat(
    val op : Int,
    val d : Int?,
)
