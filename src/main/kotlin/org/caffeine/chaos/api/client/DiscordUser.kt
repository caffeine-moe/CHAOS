package org.caffeine.chaos.api.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class DiscordUser(
    @Transient open val username: String = "",
    @Transient open val discriminator: String = "",
    @Transient open val id: String = "",
    @Transient open val avatar: String? = "",
)