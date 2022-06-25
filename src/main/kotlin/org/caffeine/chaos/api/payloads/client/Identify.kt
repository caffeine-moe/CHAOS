package org.caffeine.chaos.api.payloads.client

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.payloads.client.data.identify.IdentifyD

@Serializable
class Identify(
    override val op : Int = OPCODE.IDENTIFY.value,
    val d : IdentifyD,
) : BasePayload()