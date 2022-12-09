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
import org.caffeine.chaos.api.GATEWAY
import org.caffeine.chaos.api.OPCODE
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.connection.payloads.client.HeartBeat
import org.caffeine.chaos.api.client.connection.payloads.client.bot.identify.IdentifyDProperties
import org.caffeine.chaos.api.client.connection.payloads.client.resume.Resume
import org.caffeine.chaos.api.client.connection.payloads.client.resume.ResumeD
import org.caffeine.chaos.api.client.connection.payloads.client.user.identify.Identify
import org.caffeine.chaos.api.client.connection.payloads.client.user.identify.IdentifyD
import org.caffeine.chaos.api.client.connection.payloads.client.user.identify.IdentifyDClientState
import org.caffeine.chaos.api.client.connection.payloads.client.user.identify.IdentifyDPresence
import org.caffeine.chaos.api.client.connection.payloads.gateway.init.Init
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.ClientType
import org.caffeine.chaos.api.typedefs.ConnectionType
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import org.caffeine.chaos.api.utils.ConsoleColour
import org.caffeine.chaos.api.utils.fetchWebClientValues
import org.caffeine.chaos.api.utils.log
import java.io.ByteArrayOutputStream
import java.util.zip.Inflater
import java.util.zip.InflaterOutputStream
import kotlin.text.Charsets.UTF_8


class Connection(private val client : ClientImpl) {

    var ready = false

    private lateinit var inflater : Inflater

    private lateinit var webSocket : DefaultClientWebSocketSession

    private var heartBeat = Job() as Job

    data class PayloadDef(
        val name : String,
        val type : PayloadType,
        val payload : String,
    )

    enum class PayloadType {
        IDENTIFY,
        RESUME;
    }

    private fun generateIdentify() : PayloadDef {
        val payload =
            if (client.configuration.clientType != ClientType.BOT) json.encodeToString(generateUserIdentify()) else json.encodeToString(
                generateBotIdentify()
            )
        return PayloadDef("Identify", PayloadType.IDENTIFY, payload)
    }

    private fun generateResume() : PayloadDef {
        val payload = json.encodeToString(
            Resume(
                OPCODE.RESUME.value,
                ResumeD(
                    client.utils.gatewaySequence,
                    client.utils.sessionId,
                    client.token
                )
            )
        )
        return PayloadDef("Resume", PayloadType.RESUME, payload)

    }

    private fun generateUserIdentify() : Identify = Identify(
        OPCODE.IDENTIFY.value,
        IdentifyD(
            client.configuration.token,
            2048,
            client.utils.superProperties,
            IdentifyDPresence(
                "online",
                0,
                emptyArray(),
                false
            ),
            false,
            IdentifyDClientState()
        )
    )


    private fun generateBotIdentify() : org.caffeine.chaos.api.client.connection.payloads.client.bot.identify.Identify =
        org.caffeine.chaos.api.client.connection.payloads.client.bot.identify.Identify(
            OPCODE.IDENTIFY.value,
            org.caffeine.chaos.api.client.connection.payloads.client.bot.identify.IdentifyD(
                client.configuration.token,
                513,
                IdentifyDProperties(
                    "Windows",
                    "CHAOS",
                    "CHAOS"
                )
            )
        )

    suspend fun execute(type : ConnectionType) {
        when (type) {
            ConnectionType.CONNECT -> {
                connect(generateIdentify())
            }

            ConnectionType.DISCONNECT -> {
                disconnect()
                client.eventBus.produceEvent(ClientEvent.End)
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
                    client.utils.gatewaySequence
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
            delay(interval)
        }
    }

    private suspend fun readSocket(start : Long) {
        webSocket.incoming.receiveAsFlow().buffer(Channel.UNLIMITED).collect {
            when (it) {
                is Frame.Binary, is Frame.Text -> handleJsonRequest(it.deflateData(), client, start)
                else -> { /*ignore*/
                }
            }
        }
    }

    private suspend fun connect(payload : PayloadDef) {
        fetchWebClientValues(client)
        client.utils.createSuperProperties()
        client.utils.tokenValidator(client.configuration.token)

        inflater = Inflater()

        client.httpClient.client.wss(
            host = if (payload.type != PayloadType.RESUME) GATEWAY
            else client.utils.resumeGatewayUrl.removePrefix("wss://").ifBlank { GATEWAY },
            path = "/?encoding=json&v=9&compress=zlib-stream",
            port = 443
        ) {

            val now = System.currentTimeMillis()

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

                    send(Frame.Text(payload.payload))

                    log("${payload.name} sent.", "API:", LogLevel(LoggerLevel.LOW, client))

                    readSocket(now)
                }

                else -> {
                    println(init)
                }
            }
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
        connect(generateResume())
    }

    private suspend fun reconnect() {
        disconnect()
        execute(ConnectionType.CONNECT)
    }
}
