package org.caffeine.chaos.api

import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client

const val GATEWAY = "gateway.discord.gg"
const val BASE_URL = "https://discord.com/api/v9"

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}
val jsonc = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}
val jsonp = Json { prettyPrint = true }