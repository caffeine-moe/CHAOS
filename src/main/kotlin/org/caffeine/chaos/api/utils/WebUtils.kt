package org.caffeine.chaos.api.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.typedefs.LogLevel
import org.caffeine.chaos.api.typedefs.LoggerLevel
import kotlin.math.absoluteValue

val normalHTTPClient : HttpClient = HttpClient(CIO) {
    install(WebSockets)
    install(HttpCookies)
    install(HttpCache)
    install(ContentNegotiation)
    engine {
        pipelining = true
    }
    expectSuccess = true
}

@Serializable
data class DUAProp(
    val chrome_user_agent : String,
    val chrome_version : String,
    val client_build_number : Int,
)

var clientVersion = "114.0.0.0"
var clientBuildNumber = 203670
var userAgent =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.3"

suspend fun fetchWebClientValues(client : Client) {
    try {
        val dua = json.decodeFromString<DUAProp>(
            normalHTTPClient.get("https://cordapi.dolfi.es/api/v1/properties/web")
                .bodyAsText()
        )
        clientVersion = dua.chrome_version
        clientBuildNumber = dua.client_build_number
        userAgent = dua.chrome_user_agent
    } catch (e : ResponseException) {
        log(
            "Unable to fetch client values, falling back to default values.",
            "API:",
            LogLevel(LoggerLevel.LOW, client)
        )
    }
}

fun webkitBoundary() : String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    val random = (1..16)
        .map { charset.random() }
        .joinToString("")
    return "WebKitFormBoundary$random"
}

fun calcNonce(id : Long = 0) : String {
    val unixTs = if (id == 0L) System.currentTimeMillis() else id
    return ((unixTs - 1420070400000) * 4194304).absoluteValue.toString()
}

fun convertIdToUnix(id : String) : Long {
    return if (id.isNotBlank()) {
        (id.toLong() / 4194304 + 1420070400000).absoluteValue
    } else {
        0
    }
}