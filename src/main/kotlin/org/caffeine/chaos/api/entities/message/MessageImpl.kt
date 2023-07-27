package org.caffeine.chaos.api.entities.message

import kotlinx.coroutines.CompletableDeferred
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.GuildChannel
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.message.embeds.MessageEmbed
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.api.typedefs.PermissionType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageSendData

data class MessageImpl(
    override var client : Client,
    override var id : Snowflake,
    override val channel : TextBasedChannel,
    override val author : User,
    override var content : String = "",
    override var editedAt : Long? = null,
    override var tts : Boolean = false,
    override var mentionedEveryone : Boolean = false,
    override var mentions : HashMap<String, User> = hashMapOf(),
    override var pinned : Boolean = false,
    override var nonce : String,
    override var type : MessageType = MessageType.DEFAULT,
    override val embeds : List<MessageEmbed>,
) : Message {

    override var attachments : Map<Snowflake, MessageAttachment> = hashMapOf()

    override val mentionsSelf : Boolean = mentions.containsValue(client.user)

    override var timestamp : Long = id.timestamp.toEpochMilliseconds()

    override val guild : Guild? = if (channel is GuildChannel) {
        channel.guild
    } else null

    override suspend fun delete() : Boolean {
        return if (guild != null && this.author != client.user && !client.user.fetchGuildMember(
                client.user,
                this.guild
            ).permissions.contains(PermissionType.MANAGE_MESSAGES)
        ) false
        else client.user.deleteMessage(this).isNotBlank()
    }

    override suspend fun edit(edit : MessageSendData) : CompletableDeferred<Message> {
        if (this.author != client.user) Throwable("Unable to edit other people's messages dumbass.")
        return client.user.editMessage(this, edit)
    }

    override suspend fun edit(text : String) : CompletableDeferred<Message> {
        if (this.author != client.user) Throwable("Unable to edit other people's messages dumbass.")
        return client.user.editMessage(this, MessageBuilder().append(text))
    }

    override suspend fun reply(text : MessageSendData) : CompletableDeferred<Message> {
        return client.user.replyMessage(this, text)
    }

    override suspend fun reply(text : String) : CompletableDeferred<Message> {
        return client.user.replyMessage(this, MessageBuilder().append(text))
    }
}