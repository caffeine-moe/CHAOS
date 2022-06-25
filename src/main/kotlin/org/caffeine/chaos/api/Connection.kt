package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.payloads.client.HeartBeat
import org.caffeine.chaos.api.payloads.client.Identify
import org.caffeine.chaos.api.payloads.client.Resume
import org.caffeine.chaos.api.payloads.client.data.identify.IdentifyD
import org.caffeine.chaos.api.payloads.client.data.resume.ResumeD
import org.caffeine.chaos.api.payloads.gateway.Init
import org.caffeine.chaos.api.utils.*
import org.caffeine.chaos.log

@Serializable
class Connection {

    @Contextual
    lateinit var httpClient : HttpClient

    @Contextual
    lateinit var ws : DefaultClientWebSocketSession

    @Contextual
    lateinit var client : Client

    private var hb = Job() as Job

    private data class PayloadDef(
        val name : String,
        val payload : String,
    )

    suspend fun execute(type : ConnectionType, client : Client) {
        this.client = client
        fetchWebClientValues()
        createSuperProperties()
        val payload = when (type) {
            ConnectionType.CONNECT -> {
                val identify = json.encodeToString(Identify(OPCODE.IDENTIFY.value,
                    IdentifyD(client.config.token, superProperties)))
                println(identify)
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
        log("\u001B[38;5;33mInitialising gateway connection...", "API:")
        httpClient = ConnectionHTTPClient().httpClient
        httpClient.wss(
            host = GATEWAY,
            path = "/?v=9&encoding=json",
            port = 443
        ) {
            ws = this@wss
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            println(event.decodeToString())
            val init = jsonc.decodeFromString<Init>(event.decodeToString())
            when (init.op) {
                OPCODE.HELLO.value -> {
                    try {
                        log("Client received OPCODE 10 HELLO, sending ${payload.name} payload and starting heartbeat.",
                            "API:")
                        tokenValidator(client.config.token)
                        hb = launch { startHeartBeat(init.d.heartbeat_interval) }
                        hb.start()
                        ws.send(payload.payload)
                        log("${payload.name} sent.", "API:")
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            launch {
                                receiveJsonRequest(receivedText, this@Connection, client)
                            }
                        }
                    } catch (e : Exception) {
                        println(e)
                        e.printStackTrace()
                    }
                }
                else -> {
                    log(event.decodeToString())
                    return@wss
                }
            }
        }
    }

    suspend fun sendHeartBeat() {
        try {
            var heartbeat = json.encodeToString(HeartBeat(1, "null"))
            if (gatewaySequence > 0) {
                heartbeat = json.encodeToString(HeartBeat(1, "$gatewaySequence"))
            }
            println(heartbeat)
            ws.send(heartbeat)
        } catch (e : Exception) {
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
        log("Client logged out.", "API:")
    }

    private suspend fun reconnect() {
        disconnect()
        execute(ConnectionType.CONNECT, client)
    }
}