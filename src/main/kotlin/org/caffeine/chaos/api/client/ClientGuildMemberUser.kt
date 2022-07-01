package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
abstract class ClientGuildMemberUser(
    override val avatar : String? = "",
    val bot : Boolean = false,
    override val discriminator : String = "",
    override val id : String = "",
    val public_flags : Int = 0,
    override val username : String = "",
) : DiscordUser