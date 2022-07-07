package org.caffeine.chaos.api.payloads.client

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.payloads.client.data.lazyguild.LazyGuildD

@Serializable
data class LazyGuild(
    override val op : Int = OPCODE.LAZY_GUILD.value,
    val d : LazyGuildD = LazyGuildD(),
) : BasePayload()