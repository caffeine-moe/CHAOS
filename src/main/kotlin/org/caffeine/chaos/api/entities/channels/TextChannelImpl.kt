package org.caffeine.chaos.api.entities.channels

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.message.Message
import org.caffeine.chaos.api.entities.message.MessageFilters
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageSendData

class TextChannelImpl(
    override var id : Snowflake,
    override var client : Client,
    override var type : ChannelType = ChannelType.TEXT,
    override var lastMessageId : Snowflake,
    override var name : String = "",
    override var position : Int = 0,
    override var parentId : Snowflake,
    override var topic : String = "",
    override var nsfw : Boolean = false,
) : TextChannel {

    var guildId : Snowflake = Snowflake(0)

    override var guild : Guild
        get() = runBlocking(client.coroutineContext) { client.user.fetchGuild(guildId) }
        set(value) {
            guildId = value.id
        }

    override suspend fun sendMessage(data : MessageSendData) : CompletableDeferred<Message> =
        client.user.sendMessage(this, data)

    override suspend fun sendMessage(text : String) : CompletableDeferred<Message> =
        client.user.sendMessage(this, MessageBuilder().append(text))


    override suspend fun fetchHistory(messageFilters : MessageFilters) : List<Message> =
        client.user.fetchMessagesFromChannel(this, messageFilters)


    override suspend fun fetchMessageById(id : String) : Message =
        client.user.fetchMessageById(id, this)

    override suspend fun delete() =
        client.user.deleteChannel(this)
}