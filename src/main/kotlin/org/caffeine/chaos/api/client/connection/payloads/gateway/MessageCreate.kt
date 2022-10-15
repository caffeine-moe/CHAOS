package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Serializable

@Serializable
data class MessageCreate(
    val d : SerialMessage,
    override val op : Int,
    override val s : Int?,
    override val t : String?,
) : BasePayload()
