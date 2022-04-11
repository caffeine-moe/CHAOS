package org.caffeine.chaos.api.client

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import kotlin.properties.Delegates

class ClientFriends() {
    @Serializable
    private data class Friend(
        val user: User
    )
    @Serializable
    private data class User (
        val username: String,
        val discriminator: Int,
        val id: String,
        val avatar: String?
    )
    suspend fun getAmount(config: Config): Int {
        var number = 0
        val response = httpclient.request<String>("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<Friend>>(response)
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }
    suspend fun getList(config: Config): StringBuilder {
        val sb = StringBuilder()
        val response = httpclient.request<String>("$BASE_URL/users/@me/relationships") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<Friend>>(response)
        for ((count) in final.withIndex()) {
            sb.appendLine("${final[count].user.username}#${final[count].user.discriminator}")
        }
        return sb
    }
}