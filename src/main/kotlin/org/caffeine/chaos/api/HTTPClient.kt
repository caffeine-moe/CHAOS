package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.utils.*
import io.ktor.http.*

val GATEWAY = "gateway.discord.gg"
val BASE_URL = "https://discord.com/api/v8"

val httpclient: HttpClient = HttpClient(CIO) {
    install(WebSockets)
    install(HttpCookies)
    install(HttpCache)
    install(ContentNegotiation)
    install(HttpRequestRetry) {
        maxRetries = 5
        retryIf { _, response ->
            response.status.value == 429
        }
        delayMillis(respectRetryAfterHeader = true) { delay ->
            delay * 1000L
        }
    }
    BrowserUserAgent()
    engine {
        pipelining = true
    }
    expectSuccess = true
}