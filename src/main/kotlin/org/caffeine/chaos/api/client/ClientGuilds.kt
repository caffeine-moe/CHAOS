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

class ClientGuilds{
    var amount by Delegates.notNull<Int>()
    lateinit var list: StringBuilder
        @Serializable
        private data class Guild(
            val name: String,
            val id: String
        )
    suspend fun getAmount(config: Config): Int {
        var number = 0
        val response = httpclient.request<String>("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<Guild>>(response)
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }
    suspend fun getList(config: Config): StringBuilder {
        val sb = StringBuilder()
        val response = httpclient.request<String>("$BASE_URL/users/@me/guilds") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<Guild>>(response)
        for ((count) in final.withIndex()) {
            sb.appendLine(final[count].name)
        }
        return sb
    }
}