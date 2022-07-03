package org.caffeine.chaos.api.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.HypeSquadHouseType
import org.caffeine.chaos.api.typedefs.StatusType
import java.util.*
import kotlin.math.absoluteValue
import kotlin.system.exitProcess

class DiscordUtils {

    lateinit var token : String

    @Serializable
    data class SuperProperties(
        var os : String = "",
        var browser : String = "",
        var device : String = "",
        var browser_user_agent : String = "",
        var browser_version : String = "",
        var os_version : String = "",
        var referrer : String = "",
        var referring_domain : String = "",
        var referrer_current : String = "",
        var referring_domain_current : String = "",
        var release_channel : String = "",
        var system_locale : String = "",
        var client_build_number : Int = 0,
        var client_event_source : JsonObject = json.decodeFromString("{}"),
    )

    var gatewaySequence = 0

    var sessionId = ""

/*
    Http Client
 */

    val discordHTTPClient : HttpClient = HttpClient(CIO) {
        install(WebSockets)
        install(HttpCookies)
        install(HttpCache)
        install(ContentNegotiation)
        install(DefaultRequest)
        install(HttpRequestRetry) {
            maxRetries = 5
            retryIf { _, response ->
                response.status.value == 429
            }
            delayMillis(respectRetryAfterHeader = true) { delay ->
                delay * 1000L
            }
        }

        defaultRequest {
            port = 443
            headers {
                append("Accept-Language", "en-US")
                append("Authorization", token)
                append("Cache-Control", "no-cache")
                append("Connection", "keep-alive")
                append("Origin", "https://discord.com")
                append("Pragma", "no-cache")
                append("Referer", "https://discord.com/channels/@me")
                append("Sec-CH-UA", "\"(Not(A:Brand\";v=\"8\", \"Chromium\";v=\"$clientVersion\"")
                append("Sec-CH-UA-Mobile", "?0")
                append("Sec-CH-UA-Platform", "Windows")
                append("Sec-Fetch-Dest", "empty")
                append("Sec-Fetch-Mode", "cors")
                append("Sec-Fetch-Site", "same-origin")
                append("User-Agent", userAgent)
                append("X-Discord-Locale", "en-US")
                append("X-Debug-Options", "bugReporterEnabled")
                append("X-Super-Properties", superPropertiesB64)
            }
        }
        engine {
            pipelining = true
        }
        expectSuccess = true

        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, request ->
                if (cause.localizedMessage.contains("401 Unauthorized.")) {
                    log("Invalid token, please update your config with a valid token.", "API:")
                    exitProcess(69)
                }
                log("Error: ${cause.message} Request: ${request.content}", "API:")
                return@handleResponseExceptionWithRequest
            }
        }
    }

    fun calcNonce(id : Long = 0) : Long {
        val unixTs = if (id == 0L) System.currentTimeMillis() else id
        return ((unixTs - 1420070400000) * 4194304).absoluteValue
    }

    fun convertIdToUnix(id : String) : Long {
        return (id.toLong() / 4194304 + 1420070400000).absoluteValue
    }

    suspend fun tokenValidator(token : String) {
        discordHTTPClient.get("$BASE_URL/users/@me") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    fun getStatusType(type : String) : StatusType {
        StatusType.values().forEach {
            if (it.value == type.lowercase()) {
                return it
            }
        }
        return StatusType.UNKNOWN
    }

    fun getChannelType(type : Number) : ChannelType {
        ChannelType.values().forEach {
            if (it.ordinal == type) {
                return it
            }
        }
        return ChannelType.UNKNOWN
    }

    fun getHouseType(type : String) : HypeSquadHouseType {
        HypeSquadHouseType.values().forEach {
            if (it.value == type.lowercase()) {
                return it
            }
        }
        return HypeSquadHouseType.UNKNOWN
    }

    fun getHouseType(type : Number) : HypeSquadHouseType {
        HypeSquadHouseType.values().forEach {
            if (it.ordinal == type) {
                return it
            }
        }
        return HypeSquadHouseType.UNKNOWN
    }


/*
    Super Properties Stuff
 */

    var superProperties = SuperProperties()
    var superPropertiesStr = ""
    var superPropertiesB64 = ""

    fun createSuperProperties() {
        superProperties = SuperProperties("Windows",
            "Chrome",
            "",
            userAgent,
            clientVersion,
            "10",
            "",
            "",
            "",
            "",
            "stable",
            "en-US",
            clientBuildNumber)
        superPropertiesStr = json.encodeToString(superProperties)
        superPropertiesB64 = Base64.getEncoder().encodeToString(superPropertiesStr.toByteArray())
    }
}