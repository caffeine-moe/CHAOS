package org.caffeine.chaos.api.client

import org.caffeine.chaos.api.client.connection.Connection
import org.caffeine.chaos.api.utils.DiscordUtils

internal interface BaseClientImpl : BaseClient {
    val socket : Connection
    val utils : DiscordUtils
    val eventBus : EventBus
}