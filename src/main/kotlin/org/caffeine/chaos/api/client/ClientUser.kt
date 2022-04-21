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
        val guilds: ClientGuilds,
        val channels: ClientChannels,
) {
    val discriminatedName = "$username#$discriminator"
    fun avatarUrl(): String {
        var av = ""
        if (!avatar.isNullOrBlank()) {
            av = "https://cdn.discordapp.com/avatars/$id/$avatar"
        }
        return av
    }
}