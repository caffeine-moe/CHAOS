package org.caffeine.chaos.api.discord.client

@kotlinx.serialization.Serializable
data class ClientFriend(
    override val username : String,
    override val discriminator : String,
    override val id : String,
    override val avatar : String?,
) : DiscordUser() {
    override val discriminatedName = "$username#$discriminator"
}