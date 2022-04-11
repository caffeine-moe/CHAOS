package org.caffeine.chaos.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.websocket.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.caffeine.chaos.Config
import org.caffeine.chaos.Log
import org.caffeine.chaos.LogV2
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
    install(JsonFeature)
    engine {
        buildHeaders {
            append(HttpHeaders.UserAgent,
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
        }
    }
}

private var cfg = Json.decodeFromString<Config>(File("config.json").readText())

private val properties = JsonObject(
    mapOf(
        "\$os" to JsonPrimitive("Linux"),
        "\$browser" to JsonPrimitive("Chrome"),
        "\$device" to JsonPrimitive("")
    )
)

private val d = JsonObject(
    mapOf(
        "token" to JsonPrimitive(cfg.token),
        "properties" to JsonObject(properties)

    )
)

private val payload = JsonObject(
    mapOf(
        "op" to JsonPrimitive(2),
        "d" to JsonObject(d),
    )
)

class Connection(client: Client) {

    lateinit var ws: DefaultClientWebSocketSession

    suspend fun login(config: Config, client: Client) {
        val ws = httpclient.ws(
            method = HttpMethod.Get,
            host = GATEWAY,
            path = "/?v=8&encoding=json",
            port = 8080
        ) {
            Log("\u001B[38;5;47mConnected!")
            ws = this@ws
            val event = this@ws.incoming.receive().data
            val pl = Json.decodeFromString<rpayload>(event.decodeToString())
            when (pl.op) {
                10 -> {
                    LogV2("Gateway sent opcode 10 HELLO, sending identification payload and starting heartbeat.",
                        "API:")
                    launch { sendHeartBeat(pl.d.heartbeat_interval, this@ws) }
                    sendJsonRequest(this, payload)
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        launch {
                            recieveJsonRequest(receivedText, config, this@ws, client)
                        }
                    }
                }
            }
        }
    }
    suspend fun logout() {
        ws.close()
        LogV2("Client logged out.","API:")
    }
}
