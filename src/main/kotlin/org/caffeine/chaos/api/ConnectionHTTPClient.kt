package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*

class ConnectionHTTPClient {
    val httpclient: HttpClient = HttpClient(CIO) {
        install(WebSockets)
        install(HttpCookies)
        install(HttpCache)
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
}