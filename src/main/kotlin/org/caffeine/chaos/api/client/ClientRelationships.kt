package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class ClientRelationships(
    val friends : ClientFriends,
    val blockedUsers : ClientBlockedUsers,
)