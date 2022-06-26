package org.caffeine.chaos.api.discord.client

@kotlinx.serialization.Serializable
data class ClientGuildMemberUser(
    override val avatar : String? = "",
    val bot : Boolean = false,
    override val discriminator : String = "",
    override val id : String = "",
    val public_flags : Int = 0,
    override val username : String = "",
) : DiscordUser()