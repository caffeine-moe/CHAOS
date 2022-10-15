package org.caffeine.chaos.api

import kotlinx.serialization.json.Json

internal const val GATEWAY = "gateway.discord.gg"
internal const val BASE_URL = "https://discord.com/api/v9"

internal val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    coerceInputValues = true
    prettyPrint = true
}
