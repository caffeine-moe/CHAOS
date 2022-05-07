package org.caffeine.chaos.api.client

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.discordHTTPClient

@kotlinx.serialization.Serializable
data class ClientChannel(
    val id: String,
    val type: Int,
    val last_message_id: String?,
    var recipients: List<ClientChannelRecipient>,
    var name: String? = null,
    var icon: String? = null,
    var owner_id: String? = null,
    var client: Client,
) {
    suspend fun delete() {
        discordHTTPClient.request("$BASE_URL/channels/${this.id}") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, client.config.token)
            }
        }
    }
}