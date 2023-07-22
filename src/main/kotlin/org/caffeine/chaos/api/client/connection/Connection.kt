package org.caffeine.chaos.api.client.connection

import io.ktor.client.plugins.websocket.*
import io.ktor.utils.io.core.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.client.HeartBeat
import org.caffeine.chaos.api.client.connection.payloads.gateway.init.Init
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.ConnectionType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.*
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterOutputStream
import kotlin.text.Charsets.UTF_8
import kotlin.time.Duration.Companion.milliseconds


class Connection(private val client : ClientImpl) {

    var startTime : Long = 0
    var ready = false
    var gatewaySequence = 0
    var resumeGatewayUrl : String = ""
    var sessionId = ""

    private lateinit var inflater : Inflater

    private lateinit var webSocket : DefaultClientWebSocketSession

    private var heartBeat = Job() as Job

    suspend fun execute(type : ConnectionType) {
        when (type) {
            ConnectionType.CONNECT -> {
                connect(client.utils.generateIdentify())
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
                reconnectResume()
            }
        }
    }

    suspend fun sendHeartBeat() {
        try {
            val heartbeat = json.encodeToString(
                HeartBeat(
                    OPCODE.HEARTBEAT.value,
                    gatewaySequence
                )
            )
            webSocket.send(heartbeat)
        } catch (e : CancellationException) {
            reconnect()
        }
    }

    private suspend fun startHeartBeat(interval : Long) {
        log("Heartbeat started.", "API:", LogLevel(LoggerLevel.LOW, client))
        while (true) {
            sendHeartBeat()
            delay(interval.milliseconds)
        }
    }

    private suspend fun readSocket() {
        webSocket.incoming.receiveAsFlow().buffer(Channel.UNLIMITED).collect {
            when (it) {
                is Frame.Binary, is Frame.Text -> handleJsonRequest(it.deflateData(), client)
                else -> { /*ignore*/
                }
            }
        }
    }

    private suspend fun connect(payload : DiscordUtils.PayloadDef) {
        fetchWebClientValues(client)
        client.utils.createSuperProperties()
        if (client.type != ClientType.BOT) client.utils.tokenValidator(client.configuration.token)

        inflater = Inflater()

        client.httpClient.client.wss(
            host = if (payload.type != DiscordUtils.PayloadType.RESUME) GATEWAY
            else resumeGatewayUrl.removePrefix("wss://").ifBlank { GATEWAY },
            path = "/?encoding=json&v=${client.configuration.gatewayVersion.value}&compress=zlib-stream",
            port = 443
        ) {
            startTime = System.currentTimeMillis()

            webSocket = this

            log(
                "${ConsoleColour.GREEN.value}Connected to the Discord gateway!",
                "API:",
                LogLevel(LoggerLevel.LOW, client)
            )

            val event = incoming.receive().deflateData()
            val init = json.decodeFromString<Init>(event)
            when (init.op) {
                OPCODE.HELLO.value -> {
                    log(
                        "Client received OPCODE 10 HELLO, sending ${payload.name} payload and starting heartbeat.",
                        "API:",
                        LogLevel(LoggerLevel.LOW, client)
                    )
                    heartBeat = launch { startHeartBeat(init.d.heartbeat_interval) }

                    if (payload.type == DiscordUtils.PayloadType.RESUME) delay((6000).milliseconds)

                    send(Frame.Text(payload.payload))

                    log("${payload.name} sent.", "API:", LogLevel(LoggerLevel.LOW, client))

                    readSocket()
                }

                else -> {
                    println(init)
                }
            }
            awaitCancellation()
        }
    }

    private fun Frame.deflateData() : String {
        val outputStream = ByteArrayOutputStream()
        InflaterOutputStream(outputStream, inflater).use {
            it.write(data)
        }

        return outputStream.use {
            String(outputStream.toByteArray(), 0, outputStream.size(), UTF_8)
        }
    }

    private suspend fun disconnect() {
        heartBeat.cancelAndJoin()
        webSocket.close()
        ready = false
        log("Client logged out.", "API:", LogLevel(LoggerLevel.LOW, client))
    }

    private suspend fun reconnectResume() {
        disconnect()
        connect(client.utils.generateResume())
    }

    private suspend fun reconnect() {
        disconnect()
        execute(ConnectionType.CONNECT)
    }
}