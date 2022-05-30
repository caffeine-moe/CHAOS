package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.http.*
import org.caffeine.chaos.api.discordHTTPClient
import org.caffeine.chaos.api.token
import java.util.concurrent.CompletableFuture

@kotlinx.serialization.Serializable
class Message(
    val id: String = "",
    val content: String = "",
    val channel_id: String = "",
    val author: MessageAuthor = MessageAuthor("", "", "", ""),
    val attachments: List<MessageAttachment> = mutableListOf(),
    val embeds: List<MessageEmbed> = mutableListOf(),
    val mention_everyone: Boolean = false,
    val mention_roles: List<String> = mutableListOf(),
    val mentions: List<MessageMention> = mutableListOf(),
    val pinned: Boolean = false,
    val referenced_message: Message? = null,
    val type: Int = 0,
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