package org.caffeine.chaos.api.client.connection.payloads.client.user.identify

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.client.connection.payloads.client.BasePayload

@Serializable
class Identify(
    override val op : Int = OPCODE.IDENTIFY.value,
    val d : IdentifyD,
) : BasePayload()
