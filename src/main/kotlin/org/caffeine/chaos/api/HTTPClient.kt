package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.utils.*
import io.ktor.http.*

val GATEWAY = "gateway.discord.gg"
val BASE_URL = "https://discord.com/api/v8"

val httpclient: HttpClient = HttpClient(CIO) {
    install(WebSockets)
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
    engine {
        buildHeaders {
            append(HttpHeaders.UserAgent,
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
        }
        pipelining = true
    }
    expectSuccess = true
}