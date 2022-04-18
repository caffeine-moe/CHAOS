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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config
import org.caffeine.chaos.log
import org.caffeine.chaos.api.client.Client
import java.io.File

@Serializable
data class rpayload(
    val t: String?,
    val s: String?,
    val op: Int,
    val d: rd,
)

@Serializable
data class rd(
    val heartbeat_interval: Long,
    val _trace: List<String>,
)

val httpclient = HttpClient(CIO) {
    install(WebSockets)
    install(ContentNegotiation)
    engine {
        buildHeaders {
            append(HttpHeaders.UserAgent,
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
        }
        pipelining = true
    }
}

@Serializable
data class Identify(
    val op: Int,
    val d: IdentifyD
)

@Serializable
data class IdentifyD(
    val token: String,
    val properties: IdentifyDProperties
)

@Serializable
data class IdentifyDProperties(
    @SerialName("\$os")
    val os: String,
    @SerialName("\$browser")
    val browser: String,
    @SerialName("\$device")
    val device: String
)

@Serializable
data class Resume(
    val op: Int,
    val d: ResumeD
)

@Serializable
data class ResumeD(
    val token: String,
    val session_id: String,
    val seq: Int
)

class Connection {

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
            log("\u001B[38;5;47mConnected!")
            ws = this@ws
            val event = this@ws.incoming.receive().data
            val pl = Json.decodeFromString<rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Gateway sent opcode 10 HELLO, sending identification payload and starting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    val id = Json.encodeToString(Identify(2, IdentifyD(config.token, IdentifyDProperties("Linux", "Chrome", ""))))
                    sendJsonRequest(this@Connection, id)
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
        log("Client logged out.","API:")
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
            log("\u001B[38;5;47mConnected!")
            ws = this@ws
            val event = this@ws.incoming.receive().data
            val pl = Json.decodeFromString<rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Gateway sent opcode 10 HELLO, sending identification payload and starting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    val id = Json.encodeToString(Identify(2, IdentifyD(config.token, IdentifyDProperties("Linux", "Chrome", ""))))
                    sendJsonRequest(this@Connection, id)
                    val rc = Json.encodeToString(Resume(6, ResumeD(config.token,session_id, seq)))
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
