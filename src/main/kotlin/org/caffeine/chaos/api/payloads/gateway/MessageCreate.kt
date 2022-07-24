package org.caffeine.chaos.api.payloads.gateway

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.payloads.gateway.data.SerialMessage

@Serializable
data class MessageCreate(
    val d : SerialMessage,
    override val op : Int,
    override val s : Int?,
    override val t : String?,
) : BasePayload()
