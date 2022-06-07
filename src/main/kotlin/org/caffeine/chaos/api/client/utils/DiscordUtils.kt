package org.caffeine.chaos.api.client.utils

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient

suspend fun tokenValidator(token : String) {
    discordHTTPClient.get("$BASE_URL/users/@me") {
        headers {
            append(HttpHeaders.Authorization, token)
        }
    }
}