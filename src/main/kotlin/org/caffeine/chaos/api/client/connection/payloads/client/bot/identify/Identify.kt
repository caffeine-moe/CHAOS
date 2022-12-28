package org.caffeine.chaos.api.client.connection.payloads.client.bot.identify

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.client.connection.OPCODE
import org.caffeine.chaos.api.client.connection.payloads.client.BasePayload

@Serializable
class Identify(
    override val op : Int = OPCODE.IDENTIFY.value,
    val d : IdentifyD,
) : BasePayload()