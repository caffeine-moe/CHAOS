package org.caffeine.chaos.api.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
abstract class DiscordGuild(
    @Transient open val name : String = "",
    @Transient open val id : String = "",
    @Transient open var channels : List<ClientGuildChannel> = mutableListOf(),
    @Transient open var members : List<ClientGuildMember> = mutableListOf(),
)