package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.models.BlockedUser
import org.caffeine.chaos.api.models.Friend

data class ClientUserRelationships (
    val friends: Map<String, Friend>,
    val blockedUsers: Map<String, BlockedUser>
)