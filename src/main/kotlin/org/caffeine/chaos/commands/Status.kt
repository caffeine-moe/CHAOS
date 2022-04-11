package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient

@kotlinx.serialization.Serializable
data class status(
    val status: String
)

/*fun online(event: org.caffeine.chaos.api.D, config: Config) {
    try {
        if (event.content == "${config.prefix}online") {
            runBlocking {
                httpclient.request<String>("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, config.token)
                        append("Content-Type", "application/json")
                    }
                    body = Json.encodeToJsonElement(status("online"))
                }
            }
        }
    }catch (e: Exception){
        println(e)
    }
}

fun dnd(event: org.caffeine.chaos.api.D, config: Config) {
    if (event.content == "${config.prefix}dnd" || event.content == "${config.prefix}donotdisturb") {
        runBlocking {
            httpclient.request<String>("$BASE_URL/users/@me/settings") {
                method = HttpMethod.Patch
                headers {
                    append(HttpHeaders.Authorization, config.token)
                    append("Content-Type", "application/json")
                }
                body = Json.encodeToJsonElement(status("dnd"))
            }
        }
    }
}

fun away(event: org.caffeine.chaos.api.D, config: Config) {
    if (event.content == "${config.prefix}idle" || event.content == "${config.prefix}away") {
        runBlocking {
            httpclient.request<String>("$BASE_URL/users/@me/settings") {
                method = HttpMethod.Patch
                headers {
                    append(HttpHeaders.Authorization, config.token)
                    append("Content-Type", "application/json")
                }
                body = Json.encodeToJsonElement(status("away"))
            }
        }
    }
}

fun invis(event: org.caffeine.chaos.api.D, config: Config) {
    if (event.content == "${config.prefix}offline" || event.content == "${config.prefix}invis" || event.content == "${config.prefix}invisible") {
        runBlocking {
            httpclient.request<String>("$BASE_URL/users/@me/settings") {
                method = HttpMethod.Patch
                headers {
                    append(HttpHeaders.Authorization, config.token)
                    append("Content-Type", "application/json")
                }
                body = Json.encodeToJsonElement(status("invisible"))
            }
        }
    }
}*/
