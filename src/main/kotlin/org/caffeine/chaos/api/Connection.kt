package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
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
        val presence: IdentifyDPresence = IdentifyDPresence(),
        val compress: Boolean = false,
        val client_state: IdentifyDClientState = IdentifyDClientState(),
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
    data class IdentifyDPresence(
        val status: String = "online",
        val since: Int = 0,
        val activities: Array<String> = emptyArray(),
        val afk: Boolean = false,
    )

    @Serializable
    data class IdentifyDClientState(
        @Contextual
        val guild_hashes: Empty = Json.decodeFromString("{}"),
        val highest_last_message_id: String = "0",
        val read_state_version: Int = 0,
        val user_guild_settings_version: Int = -1,
    )

    @Serializable
    class Empty

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

    var seq = 0
    var sid = ""
    var httpClient = ConnectionHTTPClient().httpclient
    lateinit var ws: DefaultClientWebSocketSession

    suspend fun connect(client: Client) {
        httpClient.ws(
            method = HttpMethod.Get,
            host = GATEWAY,
            path = "/?v=8&encoding=json",
            port = 8080
        ) {
            ws = this
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            val pl = Json.decodeFromString<Rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Gateway sent opcode 10 HELLO, sending identification payload and starting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    val id = Json.encodeToString(Identify(2,
                        IdentifyD(client.config.token, IdentifyDProperties("Linux", "Chrome", ""))))
                    sendJsonRequest(this@Connection, id)
                    log("Identification sent.", "API:")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        launch {
                            receiveJsonRequest(receivedText, this@Connection, client)
                        }
                    }
                }
            }
        }
    }

    fun disconnect() {
        httpClient.close()
        br = true
        ready = false
        log("Client logged out.", "API:")
        return
    }

    suspend fun reconnect(session_id: String, seq: Int, client: Client) {
        log("Gateway sent opcode 7 RECONNECT, reconnecting...", "API:")
        httpClient.close()
        httpClient = ConnectionHTTPClient().httpclient
        br = true
        ready = false
        log("\u001B[38;5;33mInitialising gateway connection...")
        httpClient.ws(
            method = HttpMethod.Get,
            host = GATEWAY,
            path = "/?v=8&encoding=json",
            port = 8080
        ) {
            ws = this
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            val pl = Json.decodeFromString<Rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Gateway sent opcode 10 HELLO, resending identification payload, sending resume payload and restarting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    val id = Json.encodeToString(Identify(2,
                        IdentifyD(client.config.token, IdentifyDProperties("Linux", "Chrome", ""))))
                    sendJsonRequest(this@Connection, id)
                    log("Identification payload sent.", "API:")
                    val rs = Json.encodeToString(Resume(6, ResumeD(client.config.token, session_id, seq)))
                    sendJsonRequest(this@Connection, rs)
                    log("Resume payload sent.", "API:")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        launch {
                            receiveJsonRequest(receivedText, this@Connection, client)
                        }
                    }
                }
            }
        }
    }
}
