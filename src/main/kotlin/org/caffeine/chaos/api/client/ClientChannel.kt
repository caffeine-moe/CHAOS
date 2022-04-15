package org.caffeine.chaos.api.client

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient

@kotlinx.serialization.Serializable
data class ClientChannel (
    val id: String,
    val type: Int,
    val last_message_id: String?,
    var recipients: List<ClientChannelRecipient>,
    var name: String? = null,
    var icon: String? = null,
    var owner_id: String? = null
){
    suspend fun delete(config: Config){
        httpclient.request("$BASE_URL/channels/${this.id}") {
            method = HttpMethod.Delete
            headers {
                append(HttpHeaders.Authorization, config.token)
            }
        }
    }
}