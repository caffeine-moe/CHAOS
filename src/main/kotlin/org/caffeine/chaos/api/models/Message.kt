package org.caffeine.chaos.api.models

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.channels.TextChannel
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.typedefs.MessageType
import java.util.Date
import java.util.concurrent.CompletableFuture

data class Message(
    val client : Client = Client(),
    val id : String = "",
    val channel : TextBasedChannel = TextChannel(),
    val guild : Guild? = Guild(),
    val author : User = User(),
    val content : String = "",
    val timestamp : Date = Date(),
    val editedAt : Date? = Date(),
    val tts : Boolean = false,
    val mentionedEveryone : Boolean = false,
    val mentions : Map<String, User> = mapOf(),
    val attachments : Map<String, Attachment> = mapOf(),
    val pinned : Boolean = false,
    val type : MessageType = MessageType.DEFAULT,
) {
    suspend fun delete() {
        client.user.deleteMessage(this)
    }

    suspend fun edit(edit : MessageOptions) : CompletableFuture<Message> {
        return client.user.editMessage(this, edit)
    }
}