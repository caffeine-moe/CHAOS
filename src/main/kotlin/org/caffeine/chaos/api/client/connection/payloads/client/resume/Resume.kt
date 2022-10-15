package org.caffeine.chaos.api.client.connection.payloads.client.resume

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.client.connection.payloads.client.BasePayload

@Serializable
data class Resume(
    override val op : Int = OPCODE.RESUME.value,
    val d : ResumeD = ResumeD(),
) : BasePayload()
