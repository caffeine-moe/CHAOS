package org.caffeine.chaos.api.entities.message

import arrow.core.Either
import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageData
import java.util.*

data class MessageImpl(
    override var client : Client,
    override var id : Snowflake,
    override val channel : TextBasedChannel,
    override val guild : Guild?,
    override var author : User,
    override var content : String = "",
    override var timestamp : Date = Date(),
    override var editedAt : Date? = Date(),
    override var tts : Boolean = false,
    override var mentionedEveryone : Boolean = false,
    override var mentions : HashMap<String, User> = hashMapOf(),
    override var attachments : HashMap<String, MessageAttachment> = hashMapOf(),
    override var pinned : Boolean = false,
    override var nonce : String,
    override var type : MessageType = MessageType.DEFAULT,
) : Message {

    override suspend fun delete() {
        client.user.deleteMessage(this)
    }

    override suspend fun edit(edit : MessageData) : CompletableDeferred<Message> {
        return client.user.editMessage(this, edit)
    }

    override suspend fun edit(text : String) : CompletableDeferred<Message> {
        return client.user.editMessage(this, MessageBuilder().append(text))
    }

    override suspend fun reply(text : MessageData) : CompletableDeferred<Message> {
        return client.user.replyMessage(this, text)
    }

    override suspend fun reply(text : String) : CompletableDeferred<Message> {
        return client.user.replyMessage(this, MessageBuilder().append(text))
    }
}
