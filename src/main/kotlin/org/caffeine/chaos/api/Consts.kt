package org.caffeine.chaos.api

import kotlinx.serialization.json.Json

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