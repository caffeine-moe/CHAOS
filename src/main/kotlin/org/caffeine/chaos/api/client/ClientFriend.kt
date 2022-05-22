package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class ClientFriend(
    override val username: String,
    override val discriminator: String,
    override val id: String,
    override val avatar: String?,
) : DiscordUser(username, discriminator, id, avatar) {
    override val discriminatedName = "$username#$discriminator"
}