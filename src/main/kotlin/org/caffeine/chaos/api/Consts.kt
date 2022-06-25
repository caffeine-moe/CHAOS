package org.caffeine.chaos.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val GATEWAY = "gateway.discord.gg"
val BASE_URL = "https://discord.com/api/v9"

@Serializable
class Empty

var token = ""

val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}
val jsonc = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}
val jsonp = Json { prettyPrint = true }