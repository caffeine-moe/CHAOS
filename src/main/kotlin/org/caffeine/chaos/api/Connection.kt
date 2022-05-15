package org.caffeine.chaos.api

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.websocket.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.log
import java.util.*

@Serializable
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
        val properties: SuperProperties,
        val presence: IdentifyDPresence = IdentifyDPresence(),
        val compress: Boolean = false,
        val client_state: IdentifyDClientState = IdentifyDClientState(),
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

    @Serializable
    data class DUAProp(
        val chrome_user_agent: String,
        val chrome_version: String,
        val client_build_number: Int,
    )

    @Contextual
    var httpClient = ConnectionHTTPClient(this).httpclient

    @Contextual
    lateinit var ws: DefaultClientWebSocketSession
    lateinit var client: Client

    @Serializable
    data class SuperProperties(
        var os: String,
        var browser: String,
        var device: String,
        var browser_user_agent: String,
        var browser_version: String,
        var os_version: String,
        var referrer: String,
        var referring_domain: String,
        var referrer_current: String,
        var referring_domain_current: String,
        var release_channel: String,
        var system_locale: String,
        var client_build_number: Int,
        var client_event_source: Empty = Empty(),
    )

    @Serializable
    private data class Heartbeat(
        val op: Int,
        val d: String,
    )

    private var hb = Job() as Job

    suspend fun connect(client: Client) {
        log("\u001B[38;5;33mInitialising gateway connection...", "API:")
        val prop =
            json.decodeFromString<DUAProp>(normalHTTPClient.get("https://discord-user-api.cf/api/v1/properties/web")
                .bodyAsText())
        ua = prop.chrome_user_agent
        cv = prop.chrome_version
        cbn = prop.client_build_number
        sp = json.encodeToString(SuperProperties("Windows",
            "Chrome",
            "",
            ua,
            cv,
            "10",
            "",
            "",
            "",
            "",
            "stable",
            "en-US",
            cbn))
        encsp = Base64.getEncoder().encodeToString(sp.toByteArray())
        httpClient = ConnectionHTTPClient(this).httpclient
        httpClient.wss(
            host = GATEWAY,
            path = "/?v=9&encoding=json",
            port = 443
        ) {
            ws = this@wss
            this@Connection.client = client
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            val pl = json.decodeFromString<Rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Client received OPCODE 10 HELLO, sending identification payload and starting heartbeat.",
                        "API:")
                    hb = launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) }
                    hb.start()
                    val id = json.encodeToString(Identify(2,
                        IdentifyD(client.config.token,
                            SuperProperties("Windows",
                                "Chrome",
                                "",
                                ua,
                                cv,
                                "10",
                                "",
                                "",
                                "",
                                "",
                                "stable",
                                "en-US",
                                cbn))))
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


    private suspend fun sendHeartBeat(interval: Long, connection: Connection) {
        var heartbeat = json.encodeToString(Heartbeat(1, "null"))
        log("Heartbeat started.", "API:")
        while (true) {
            if (seq > 0) {
                heartbeat = json.encodeToString(Heartbeat(1, "$seq"))
            }
            delay(interval)
            sendJsonRequest(connection, heartbeat)
        }
    }

    suspend fun disconnect() {
        this.hb.cancel()
        this.ws.close()
        ready = false
        log("Client logged out.", "API:")
    }

    suspend fun recRes(session_id: String, seq: Int, client: Client) {
        disconnect()
        log("\u001B[38;5;33mInitialising gateway connection...", "API:")
        httpClient = ConnectionHTTPClient(Connection()).httpclient
        httpClient.wss(
            host = GATEWAY,
            path = "/?v=9&encoding=json",
            port = 443
        ) {
            ws = this
            log("\u001B[38;5;47mConnected to the Discord gateway!", "API:")
            val event = this.incoming.receive().data
            val pl = json.decodeFromString<Rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    log("Client received OPCODE 10 HELLO, sending identification and resume payload then starting heartbeat.",
                        "API:")
                    hb = launch { sendHeartBeat(pl.d.heartbeat_interval, this@Connection) } as CompletableJob
                    hb.start()
                    val id = json.encodeToString(Identify(2,
                        IdentifyD(client.config.token,
                            SuperProperties("Windows",
                                "Chrome",
                                "",
                                ua,
                                cv,
                                "10",
                                "",
                                "",
                                "",
                                "",
                                "stable",
                                "en-US",
                                cbn))))
                    sendJsonRequest(this@Connection, id)
                    log("Identification sent.", "API:")
                    val rs = json.encodeToString(Resume(6, ResumeD(client.config.token, session_id, seq)))
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

    suspend fun reconnect() {
        this.disconnect()
        connect(client)
    }
}