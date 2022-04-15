package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient


class ClientChannels(){
    val groupChannels = ClientGroupChannels()
    suspend fun getAmount(config: Config): Int {
        var number = 0
        val response = httpclient.request("$BASE_URL/users/@me/channels") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
        println(response)
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<ClientChannel>>(response.body())
        for ((_) in final.withIndex()) {
            number++
        }
        return number
    }
    suspend fun getList(config: Config): StringBuilder {
        val sb = StringBuilder()
        val response = httpclient.request("$BASE_URL/users/@me/channels") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
        println(response)
        val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<ClientChannel>>(response.body())
        for ((count) in final.withIndex()) {
            sb.appendLine(final[count].id)
        }
        return sb
    }
}

