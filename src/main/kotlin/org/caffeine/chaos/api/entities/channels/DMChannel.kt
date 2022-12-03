package org.caffeine.chaos.api.entities.channels

import org.caffeine.chaos.api.entities.users.User

interface DMChannel : TextBasedChannel {
    val recipients : Map<String, User>
}