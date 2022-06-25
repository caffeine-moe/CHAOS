package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.ConsoleColours
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.payloads.client.HeartBeat
import org.caffeine.chaos.api.payloads.client.Identify
import org.caffeine.chaos.api.payloads.client.Resume
import org.caffeine.chaos.api.payloads.client.data.identify.IdentifyD
import org.caffeine.chaos.api.payloads.client.data.resume.ResumeD
import org.caffeine.chaos.api.payloads.gateway.Init
import org.caffeine.chaos.api.utils.*
import org.caffeine.chaos.log


class Connection {

    private var httpClient = ConnectionHTTPClient().httpClient

    lateinit var ws : DefaultClientWebSocketSession

    lateinit var client : Client

    private var hb = Job() as Job

    private data class PayloadDef(
        val name : String,
        val payload : String,
    )

    suspend fun execute(type : ConnectionType, client : Client) {
        fetchWebClientValues()
        createSuperProperties()
        val payload = when (type) {
            ConnectionType.CONNECT -> {
                val identify = json.encodeToString(Identify(OPCODE.IDENTIFY.value,
                    IdentifyD(client.config.token, superProperties)))
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
                val resume = json.encodeToString(Resume(
                    OPCODE.RESUME.value,
                    ResumeD(
                        gatewaySequence,
                        sessionId,
                        client.config.token
                    )
                ))
                PayloadDef("Resume", resume)
            }
        }
        httpClient = ConnectionHTTPClient().httpClient
        httpClient.wss(
            host = GATEWAY,
            path = "/?v=9&encoding=json",
            port = 443
        ) {
            ws = this@wss
            this@Connection.client = client
            log("${ConsoleColours.GREEN.value}Connected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            val init = json.decodeFromString<Init>(event.decodeToString())
            when (init.op) {
                OPCODE.HELLO.value -> {
                    log("Client received OPCODE 10 HELLO, sending ${payload.name} payload and starting heartbeat.",
                        "API:")

                    tokenValidator(client.config.token)

                    hb = launch { startHeartBeat(init.d.heartbeat_interval) }
                    hb.start()

                    send(payload.payload)
                    log("${payload.name} sent.", "API:")

                    try {
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            launch {
                                receiveJsonRequest(receivedText, this@Connection, client)
                            }
                        }
                    } catch (e : Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    suspend fun sendHeartBeat() {
        try {
        val heartbeat = json.encodeToString(HeartBeat(OPCODE.HEARTBEAT.value,
            if (gatewaySequence > 0) gatewaySequence else null))
        ws.send(heartbeat)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun startHeartBeat(interval : Long) {
        log("Heartbeat started.", "API:")
        while (true) {
            sendHeartBeat()
            delay(interval)
        }
    }

    private suspend fun disconnect() {
        this.hb.cancel()
        this.ws.close()
        ready = false
        log("Client logged out.", "API:")
    }

    private suspend fun reconnect() {
        this.disconnect()
        execute(ConnectionType.CONNECT, client)
    }

}