package org.caffeine.chaos.api.client.connection.payloads.gateway

import kotlinx.serialization.Serializable

@Serializable
data class MessageUpdate(
    val d : SerialMessageUpdate,
    override val op : Int,
    override val s : Int?,
    override val t : String?,
) : BasePayload()