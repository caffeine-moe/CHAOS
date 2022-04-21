package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.config.Config
import org.caffeine.chaos.log

class Connection {

    @Serializable
    data class Rpayload(
        val t: String?,
        val s: String?,
        val op: Int,
        val d: Rd,
    )

    @Serializable
    data class Rd(
        val heartbeat_interval: Long,
        val _trace: List<String>,
    )

    @Serializable
    data class Identify(
        val op: Int,
        val d: IdentifyD,
    )

    @Serializable
    data class IdentifyD(
        val token: String,
        val properties: IdentifyDProperties,
    )

    @Serializable
    data class IdentifyDProperties(
        @SerialName("\$os")
        val os: String,
        @SerialName("\$browser")
        val browser: String,
        @SerialName("\$device")
        val device: String,
    )

    @Serializable
    data class Resume(
        val op: Int,
        val d: ResumeD,
    )

    @Serializable
    data class ResumeD(
        val token: String,
        val session_id: String,
        val seq: Int,
    )

    var lasts = 0
    var sid = ""
    lateinit var ws: DefaultClientWebSocketSession

    suspend fun login(config: Config, client: Client) {
        httpclient.ws(
            method = HttpMethod.Get,
            host = GATEWAY,
            path = "/?v=8&encoding=json",
            port = 8080
        ) {
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            ws = this@ws
            val event = this@ws.incoming.receive().data
            val pl = Json.decodeFromString<Rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Gateway sent opcode 10 HELLO, sending identification payload and starting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    val id = Json.encodeToString(Identify(2,
                        IdentifyD(config.token, IdentifyDProperties("Linux", "Chrome", ""))))
                    sendJsonRequest(this@Connection, id)
                    log("Identification sent.", "API:")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        launch {
                            receiveJsonRequest(receivedText, config, this@Connection, client)
                        }
                    }
                }
            }
        }
    }

    suspend fun logout() {
        ws.close()
        log("Client logged out.", "API:")
    }

    suspend fun reconnect(config: Config, session_id: String, seq: Int, client: Client) {
        log("Gateway sent opcode 7 RECONNECT, reconnecting...", "API:")
        br = true
        logout()
        log("\u001B[38;5;33mInitialising gateway connection...")
        httpclient.ws(
            method = HttpMethod.Get,
            host = GATEWAY,
            path = "/?v=8&encoding=json",
            port = 8080
        ) {
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            ws = this@ws
            val event = this@ws.incoming.receive().data
            val pl = Json.decodeFromString<Rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Gateway sent opcode 10 HELLO, resending identification payload, sending resume payload and restarting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    val id = Json.encodeToString(Identify(2,
                        IdentifyD(config.token, IdentifyDProperties("Linux", "Chrome", ""))))
                    sendJsonRequest(this@Connection, id)
                    log("Identification payload sent.", "API:")
                    val rs = Json.encodeToString(Resume(6, ResumeD(config.token, session_id, seq)))
                    sendJsonRequest(this@Connection, rs)
                    log("Resume payload sent.", "API:")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        launch {
                            receiveJsonRequest(receivedText, config, this@Connection, client)
                        }
                    }
                }
            }
        }
    }
}
