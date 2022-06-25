package org.caffeine.chaos.api.utils

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.Empty
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.json
import java.util.*

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
    var client_event_source : Empty = Empty(),
)

var gatewaySequence = 0

var sessionId = ""

suspend fun tokenValidator(token : String) {
    discordHTTPClient.get("$BASE_URL/users/@me") {
        headers {
            append(HttpHeaders.Authorization, token)
        }
    }
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