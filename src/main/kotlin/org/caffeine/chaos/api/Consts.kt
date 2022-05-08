package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import kotlinx.serialization.json.Json

val GATEWAY = "gateway.discord.gg"
val BASE_URL = "https://discord.com/api/v9"

var scamlinks = listOf<String>()

var ua =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36"
var cv = "101.0.4951.54"
var cbn = 127019
var seq = 0
var sid = ""
var sp = ""
var encsp: String = ""

val json = Json { ignoreUnknownKeys = true }
val jsonc = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

val normalHTTPClient: HttpClient = HttpClient(CIO) {
    install(WebSockets)
    install(HttpCookies)
    install(HttpCache)
    install(ContentNegotiation)
    engine {
        pipelining = true
    }
    expectSuccess = true
}