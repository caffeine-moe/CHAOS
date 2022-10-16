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
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.ConnectionType
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.utils.clientVersion
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.api.utils.userAgent
import kotlin.system.exitProcess

class HTTPClient(val client : ClientImpl) {

    private val botHttpClient : HttpClient = HttpClient(CIO) {
        install(WebSockets)
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
            headers {
                append("Content-Type", "application/json")
                append("Authorization", client.configuration.token)
            }
        }
        engine {
            pipelining = true
        }

        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                if (cause.localizedMessage.contains("401 Unauthorized.")) {
                    log("Invalid token, please update your config with a valid token.", "API:")
                    client.logout()
                    return@handleResponseExceptionWithRequest
                }
                log("Error: ${cause.message} Request: ${request.content}", "API:")
                client.socket.execute(ConnectionType.RECONNECT)
            }
        }
    }


    /*
    SelfBot HTTP Client
    */

    private val userHttpClient : HttpClient = HttpClient(CIO) {
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
            headers {
                append("Accept-Language", "en-US")
                append("Authorization", client.configuration.token)
                append("Cache-Control", "no-cache")
                append("Connection", "keep-alive")
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
                append("X-Super-Properties", client.utils.superPropertiesB64)
            }
        }
        engine {
            pipelining = true
        }
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                if (cause.localizedMessage.contains("401 Unauthorized.")) {
                    log("Invalid token, please update your config with a valid token.", "API:")
                    client.logout()
                }
                log("Error: ${cause.message} Request: ${request.content}", "API:")
                return@handleResponseExceptionWithRequest
            }
        }
    }

    private val httpClient = if (client.configuration.clientType != ClientType.BOT) { userHttpClient } else {
        botHttpClient
    }

    suspend fun get(url : String, headersBuilder : HeadersBuilder = HeadersBuilder()) : CompletableDeferred<String> {
        return CompletableDeferred(httpClient.get(url){
            headersBuilder.build()
        }.bodyAsText())
    }

    suspend fun post(url : String, data : String = "", headersBuilder : HeadersBuilder = HeadersBuilder()) : CompletableDeferred<String> {
        return CompletableDeferred(httpClient.post(url){
            headers {
                append("Content-Type", "application/json")
            }
            headersBuilder.build()
            setBody(data)
        }.bodyAsText())
    }

    suspend fun patch(url : String, data : String, headersBuilder : HeadersBuilder = HeadersBuilder()) : CompletableDeferred<String> {
        return CompletableDeferred(httpClient.patch(url) {
            headers {
                append("Content-Type", "application/json")
            }
            headersBuilder.build()
            setBody(data)
        }.bodyAsText())
    }

    suspend fun delete(url : String) : CompletableDeferred<String> {
        return CompletableDeferred(httpClient.delete(url){
        }.bodyAsText())
    }

    companion object Factory {
        fun build(client : ClientImpl) : HTTPClient {
            return HTTPClient(client)
        }
    }
}