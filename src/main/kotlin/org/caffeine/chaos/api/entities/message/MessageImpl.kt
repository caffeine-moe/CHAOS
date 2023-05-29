package org.caffeine.chaos.api.entities.message

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.channels.TextBasedChannel
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.guild.GuildMemberData
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageData

data class MessageImpl(
    override var client : Client,
    override var id : Snowflake,
    override val channel : TextBasedChannel,
    override var author : User,
    override var content : String = "",
    override var editedAt : Long? = null,
    override var tts : Boolean = false,
    override var mentionedEveryone : Boolean = false,
    override var mentions : HashMap<String, User> = hashMapOf(),
    override var attachments : Map<Snowflake, MessageAttachment> = hashMapOf(),
    override var pinned : Boolean = false,
    override var nonce : String,
    override var type : MessageType = MessageType.DEFAULT,
) : Message {

    override var timestamp : Long = id.timestamp.toEpochMilliseconds()

    var guildId = Snowflake(0)
    override var guild : Guild?
        get() = runBlocking(client.coroutineContext) {
            if (guildId.isZero()) null else
                client.user.fetchGuild(guildId)
        }
        set(value) {
            guildId = value?.id ?: Snowflake(0)
        }

    override val guildMember : GuildMemberData?
        get() = runBlocking(client.coroutineContext) {
            (guild)?.let { client.user.fetchGuildMember(author, it) }
        }

    override suspend fun delete() {
        client.user.deleteMessage(this)
    }

    override suspend fun edit(edit : MessageData) : CompletableDeferred<Message> {
        if (this.author != client.user) Throwable("Unable to edit other people's messages dumbass.")
        return client.user.editMessage(this, edit)
    }

    override suspend fun edit(text : String) : CompletableDeferred<Message> {
        if (this.author != client.user) Throwable("Unable to edit other people's messages dumbass.")
        return client.user.editMessage(this, MessageBuilder().append(text))
    }

    override suspend fun reply(text : MessageData) : CompletableDeferred<Message> {
        return client.user.replyMessage(this, text)
    }

    override suspend fun reply(text : String) : CompletableDeferred<Message> {
        return client.user.replyMessage(this, MessageBuilder().append(text))
    }
}