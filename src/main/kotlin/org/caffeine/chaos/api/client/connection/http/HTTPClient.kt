package org.caffeine.chaos.api.client.connection.http

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.utils.clientVersion
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.api.utils.userAgent

class HTTPClient(val clientImpl : ClientImpl) {

    private val defaultHeaders : Headers = if (clientImpl.configuration.clientType != ClientType.BOT) {
        buildHeaders {
            append("Accept-Language", "en-US")
            append("Authorization", clientImpl.configuration.token)
            append("Cache-Control", "no-cache")
            append("Connection", "keep-alive")
            append("Content-Type", "application/json")
            append("Origin", "https://discord.com")
            append("Pragma", "no-cache")
            append("Referer", "https://discord.com/channels/@me")
            append("Sec-CH-UA", "\"(Not(A:Brand\";v=\"8\", \"Chromium\";v=\"$clientVersion\"")
            append("Sec-CH-UA-Mobile", "?0")
            append("Sec-CH-UA-Platform", "Windows")
            append("Sec-Fetch-Dest", "empty")
            append("Sec-Fetch-Mode", "cors")
            append("Sec-Fetch-Site", "same-origin")
            append("User-Agent", userAgent)
            append("X-Discord-Locale", "en-US")
            append("X-Debug-Options", "bugReporterEnabled")
            append("X-Super-Properties", clientImpl.utils.superPropertiesB64)
        }
    } else {
        buildHeaders {
            append("Authorization", clientImpl.configuration.token)
            append("Content-Type", "application/json")
        }
    }

    val client : HttpClient = HttpClient(CIO) {
        install(WebSockets)
        install(HttpCookies)
        install(HttpCache)
        install(ContentNegotiation)
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
            port = 443
            headers.appendAll(defaultHeaders)
        }

        engine {
            pipelining = true
        }
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                if (cause.localizedMessage.contains("401 Unauthorized.")) {
                    log("Invalid token!", "API:")
                    return@handleResponseExceptionWithRequest
                }
                log("Error: ${cause.message}", "API:")
                clientImpl.restart()
            }
        }
    }

    suspend fun get(url : String, headersBuilder : HeadersBuilder = HeadersBuilder()) : CompletableDeferred<String> {
        val request = sendRequest(request {
            url(url)
            method = HttpMethod.Get
            headers.appendAll(headersBuilder.build())
        })
        return CompletableDeferred(request)
    }

    suspend fun post(
        url : String,
        data : String = "",
        headersBuilder : HeadersBuilder = HeadersBuilder(),
    ) : CompletableDeferred<String> {
        val request = sendRequest(request {
            url(url)
            method = HttpMethod.Post
            headers.appendAll(headersBuilder.build())
            setBody(data)
        })
        return CompletableDeferred(request)
    }

    suspend fun patch(
        url : String,
        data : String,
        headersBuilder : HeadersBuilder = HeadersBuilder(),
    ) : CompletableDeferred<String> {
        val request = sendRequest(request {
            url(url)
            method = HttpMethod.Patch
            headers.appendAll(headersBuilder.build())
            setBody(data)
        })
        return CompletableDeferred(request)
    }

    suspend fun delete(url : String) : CompletableDeferred<String> {
        val request = sendRequest(request {
            url(url)
            method = HttpMethod.Delete
        })
        return CompletableDeferred(request)
    }

    suspend fun sendRequest(data : HttpRequestBuilder) : String {
        return client.request {
            takeFrom(data)
        }.bodyAsText()
    }

    companion object Factory {
        fun build(client : ClientImpl) : HTTPClient {
            return HTTPClient(client)
        }
    }
}