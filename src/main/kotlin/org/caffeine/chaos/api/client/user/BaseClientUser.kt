package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.models.Guild

interface BaseClientUser {
    val guilds : Map<String, Guild>
}