package org.caffeine.chaos.api.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient

class ClientGroupChannels {
    suspend fun getList(config: Config) : MutableList<ClientChannel>{
        var list = mutableListOf<ClientChannel>()
        val response = httpclient.request("$BASE_URL/users/@me/channels") {
                method = HttpMethod.Get
                headers {
                    append(HttpHeaders.Authorization, config.token)
                }
            }
            val final = Json { ignoreUnknownKeys = true }.decodeFromString<List<ClientChannel>>(response.body())
        for ((count) in final.withIndex()) {
                if (final[count].type == 3){
                    list.add(final[count])
                }
            }
        return list
        }
}