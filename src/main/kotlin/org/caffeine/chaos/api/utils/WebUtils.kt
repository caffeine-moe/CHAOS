package org.caffeine.chaos.api.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.json

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

var clientVersion = "106.0.5249.91"
var clientBuildNumber = 150748
var userAgent =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.5249.91 Safari/537.36"
//Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36
suspend fun fetchWebClientValues() {
    val dua = json.decodeFromString<DUAProp>(
        normalHTTPClient.get("https://discord-user-api.cf/api/v1/properties/web")
            .bodyAsText()
    )
    clientVersion = dua.chrome_version
    clientBuildNumber = dua.client_build_number
    userAgent = dua.chrome_user_agent
}

fun webkitBoundary() : String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    val random = (1..16)
        .map { charset.random() }
        .joinToString("")
    return "WebKitFormBoundary$random"
}