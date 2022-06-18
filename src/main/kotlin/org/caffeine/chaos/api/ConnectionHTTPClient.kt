package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import org.caffeine.chaos.log

data class ConnectionHTTPClient(val connection : Connection) {
    val httpclient : HttpClient = HttpClient(CIO) {
        install(WebSockets)
        install(HttpCookies)
        install(HttpCache)
        install(DefaultRequest)
        install(HttpRequestRetry) {
            maxRetries = 5
            retryIf { _, response ->
                response.status.value == 429
            }
            delayMillis(respectRetryAfterHeader = true) { delay ->
                delay * 1000L
            }
        }
        defaultRequest {
            headers {
                append("Accept-Language", "en-US")
                append("Cache-Control", "no-cache")
                append("Connection", "keep-alive")
                append("Origin", "https://discord.com")
                append("Pragma", "no-cache")
                append("Referer", "https://discord.com/channels/@me")
                append("Sec-CH-UA", "\"(Not(A:Brand\";v=\"8\", \"Chromium\";v=\"$cv\"")
                append("Sec-CH-UA-Mobile", "?0")
                append("Sec-CH-UA-Platform", "Windows")
                append("Sec-Fetch-Dest", "empty")
                append("Sec-Fetch-Mode", "cors")
                append("Sec-Fetch-Site", "same-origin")
                append("User-Agent", ua)
                append("X-Discord-Locale", "en-US")
                append("X-Debug-Options", "bugReporterEnabled")
                append("X-Super-Properties", encsp)
            }
        }
        engine {
            pipelining = true
        }
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                log("Error: ${cause.message}", "API:")
                return@handleResponseExceptionWithRequest
            }
        }
    }
}