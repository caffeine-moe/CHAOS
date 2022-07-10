package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.models.User

data class ClientUserRelationships (
    val friends: List<User>,
    val blockedUsers: List<User>
)