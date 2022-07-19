package org.caffeine.chaos.api.client.connection

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.*
import org.caffeine.chaos.api.client.BaseClient
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.EventBus
import org.caffeine.chaos.api.payloads.client.HeartBeat
import org.caffeine.chaos.api.payloads.client.Identify
import org.caffeine.chaos.api.payloads.client.Resume
import org.caffeine.chaos.api.payloads.client.data.identify.IdentifyD
import org.caffeine.chaos.api.payloads.client.data.resume.ResumeD
import org.caffeine.chaos.api.payloads.gateway.Init
import org.caffeine.chaos.api.utils.*

class Connection(private val client : ClientImpl, private val eventBus : EventBus) {

    private val httpClient : HttpClient
        get() = HttpClient(CIO) {
            install(WebSockets)
            install(HttpCookies)
            install(HttpTimeout)
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
                requestTimeout = 0
            }
            expectSuccess = true

            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, request ->
                    log("Error: ${cause.message} Request: ${request.content}", "API:")
                    return@handleResponseExceptionWithRequest
                }
            }
        }

    lateinit var webSocket : DefaultClientWebSocketSession

    private var heartBeat = Job() as Job

    private data class PayloadDef(
        val name : String,
        val payload : String,
    )

    suspend fun execute(type : ConnectionType) {
        val payload = when (type) {
            ConnectionType.CONNECT -> {
                fetchWebClientValues()
                client.utils.createSuperProperties()
                val identify = json.encodeToString(Identify(
                    OPCODE.IDENTIFY.value,
                    IdentifyD(
                        client.utils.token,
                        client.utils.superProperties
                    )
                ))
                PayloadDef("Identify", identify)
            }
            ConnectionType.DISCONNECT -> {
                disconnect()
                return
            }
            ConnectionType.RECONNECT -> {
                reconnect()
                return
            }
            ConnectionType.RECONNECT_AND_RESUME -> {
                disconnect()
                fetchWebClientValues()
                client.utils.createSuperProperties()
                val resume = json.encodeToString(Resume(
                    OPCODE.RESUME.value,
                    ResumeD(
                        client.utils.gatewaySequence,
                        client.utils.sessionId,
                        client.utils.token
                    )
                ))
                PayloadDef("Resume", resume)
            }
        }
        httpClient.wss(
            host = GATEWAY,
            path = "/?v=9&encoding=json",
            port = 443
        ) {
            webSocket = this@wss
            log("${ConsoleColours.GREEN.value}Connected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            val init = json.decodeFromString<Init>(event.decodeToString())
            when (init.op) {
                OPCODE.HELLO.value -> {
                    log("Client received OPCODE 10 HELLO, sending ${payload.name} payload and starting heartbeat.",
                        "API:")

                    client.utils.tokenValidator(client.utils.token)

                    heartBeat = launch { startHeartBeat(init.d.heartbeat_interval) }
                    heartBeat.start()

                    send(payload.payload)
                    log("${payload.name} sent.", "API:")

                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        launch {
                            handleJsonRequest(receivedText, client, eventBus)
                        }
                    }
                }
            }
        }
    }

    suspend fun sendHeartBeat() {
        val heartbeat = json.encodeToString(HeartBeat(OPCODE.HEARTBEAT.value,
            if (client.utils.gatewaySequence > 0) client.utils.gatewaySequence else null))
        webSocket.send(heartbeat)
    }

    private suspend fun startHeartBeat(interval : Long) {
        log("Heartbeat started.", "API:")
        while (true) {
            sendHeartBeat()
            delay(interval)
        }
    }

    private suspend fun disconnect() {
        this.heartBeat.cancel()
        this.webSocket.close()
        client.ready = false
        log("Client logged out.", "API:")
    }

    private suspend fun reconnect() {
        this.disconnect()
        execute(ConnectionType.CONNECT)
    }

}