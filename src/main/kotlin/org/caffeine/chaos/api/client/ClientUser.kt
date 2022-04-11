package org.caffeine.chaos.api.client

data class ClientUser(
        val verified: Boolean,
        val username: String,
        val discriminator: Int,
        val id: String,
        val email: String,
        val bio: String?,
        val avatar: String?,
        val friends: ClientFriends,
        val guilds: ClientGuilds
)