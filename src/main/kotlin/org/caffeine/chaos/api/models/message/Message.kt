package org.caffeine.chaos.api.models.message

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.channels.TextChannel
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.users.User
import org.caffeine.chaos.api.typedefs.MessageOptions
import org.caffeine.chaos.api.typedefs.MessageType
import java.util.*

data class Message(
    val client : Client,
    val id : String = "",
    val channel : TextBasedChannel,
    val guild : Guild? = null,
    val author : User = User(client = client),
    val content : String = "",
    val timestamp : Date = Date(),
    val editedAt : Date? = Date(),
    val tts : Boolean = false,
    val mentionedEveryone : Boolean = false,
    val mentions : Map<String, User> = mapOf(),
    val attachments : Map<String, MessageAttachment> = mapOf(),
    val pinned : Boolean = false,
    val type : MessageType = MessageType.DEFAULT,
) {
    suspend fun delete() {
        client.user.deleteMessage(this)
    }

    suspend fun edit(edit : MessageOptions) : CompletableDeferred<Message> {
        return client.user.editMessage(this, edit)
    }

    suspend fun edit(text : String) : CompletableDeferred<Message> {
        return client.user.editMessage(this, MessageOptions(text))
    }
}
