package org.caffeine.chaos.api.payloads.client

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.payloads.client.data.resume.ResumeD

@Serializable
data class Resume(
    override val op : Int = OPCODE.RESUME.value,
    override val d : ResumeD = ResumeD(),
) : BasePayload()