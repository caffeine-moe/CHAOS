package org.caffeine.chaos.api.discord.client

@kotlinx.serialization.Serializable
data class ClientRelationships(
    val friends : MutableList<ClientFriend>,
    val blockedUsers : MutableList<ClientBlockedUser>,
)