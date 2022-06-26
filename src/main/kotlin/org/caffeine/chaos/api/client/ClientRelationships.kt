package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class ClientRelationships(
    val friends : MutableList<ClientFriend>,
    val blockedUsers : MutableList<ClientBlockedUser>,
)