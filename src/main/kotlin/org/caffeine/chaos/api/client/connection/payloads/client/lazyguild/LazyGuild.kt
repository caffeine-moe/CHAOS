package org.caffeine.chaos.api.client.connection.payloads.client.lazyguild

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.client.connection.payloads.client.BasePayload

@Serializable
data class LazyGuild(
    override val op : Int = OPCODE.LAZY_GUILD.value,
    val d : LazyGuildD = LazyGuildD(),
) : BasePayload()
