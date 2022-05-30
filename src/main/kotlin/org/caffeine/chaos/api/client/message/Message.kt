package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.token
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
data class Message(
    var id: String = "",
    var content: String = "",
    var channel_id: String = "",
    var author: MessageAuthor = MessageAuthor("", "", "", ""),
    var attachments: List<MessageAttachment> = mutableListOf(),
    var embeds: List<MessageEmbed> = mutableListOf(),
    var mention_everyone: Boolean = false,
    var mention_roles: List<String> = mutableListOf(),
    var mentions: List<MessageMention> = mutableListOf(),
    var pinned: Boolean = false,
    var referenced_message: Message? = null,
    var type: Int = 0,
) {
    suspend fun edit(message: Message): CompletableFuture<Message> {
        return editMessage(this, message)
    }

    suspend fun delete() {
        try {
            deleteMessage(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun pinMessage() {
        discordHTTPClient.request("https://discord.com/api/v9/channels/$channel_id/pins/$id") {
            method = HttpMethod.Put
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }
}