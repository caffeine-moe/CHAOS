package org.caffeine.chaos.api.client.message

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.DiscordUser
import org.caffeine.chaos.api.discordHTTPClient

@kotlinx.serialization.Serializable
data class MessageAuthor(
    override val username: String,
    override val discriminator: String,
    override val id: String,
    override val avatar: String? = "",
) : DiscordUser(username, discriminator, id, avatar) {
    val discriminatedName = "$username#$discriminator"
    suspend fun blocked(client: Client) : Boolean {
        var torf = false
        if (client.user.relationships.blockedUsers.getList().contains(this)) {
            torf = true
        }
        return torf
    }
}