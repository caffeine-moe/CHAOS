package org.caffeine.chaos.api.client

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.Connection
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageChannel
import org.caffeine.chaos.api.httpclient
import org.caffeine.chaos.config.Config
import java.util.concurrent.CompletableFuture

data class Client(
    var config: Config,
) {
    private val socket = Connection()
    lateinit var user: ClientUser
    suspend fun login(client: Client) {
        this.socket.login(client, config)
    }

    suspend fun logout() {
        this.socket.logout()
    }

    suspend fun setStatus(status: ClientStatusType) {
        when (status) {
            ClientStatusType.ONLINE -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("online")))
                }
            }
            ClientStatusType.IDLE -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("idle")))
                }
            }
            ClientStatusType.DND -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("dnd")))
                }
            }
            ClientStatusType.INVISIBLE -> {
                httpclient.request("$BASE_URL/users/@me/settings") {
                    method = HttpMethod.Patch
                    headers {
                        append(HttpHeaders.Authorization, config.token)
                        append("Content-Type", "application/json")
                    }
                    setBody(Json.encodeToString(ClientStatus("invisible")))
                }
            }
        }
    }

    suspend fun sendMessage(channel: MessageChannel, message: Message): CompletableFuture<Message> {
        return org.caffeine.chaos.api.client.message.sendMessage(channel, message, channel.client)
    }

    suspend fun validateChannelId(id: String): Boolean {
        var valid = false
        val response = httpclient.request("$BASE_URL/channels/${id}/messages?limit=1") {
            method = HttpMethod.Get
            headers {
                append(HttpHeaders.Authorization, config.token)
                append("Content-Type", "application/json")
            }
        }
        if (response.status.isSuccess()) {
            valid = true
        }
        return valid
    }
}
