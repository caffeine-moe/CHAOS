package org.caffeine.chaos.api.models.channels

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.typedefs.ChannelType
import org.caffeine.chaos.api.typedefs.MessageOptions
import java.util.*

class TextChannel(
    override val id : String = "",
    override val client : Client = Client(),
    override val type : ChannelType = ChannelType.TEXT,
    override val lastMessageId : String = "",
    override val lastPinTimestamp : Date = Date(),
    override val name : String = "",
    override val position : Number = 0,
    override val parentId : String = "",
    override val topic : String = "",
    override val permissionOverwrites : Array<Any> = arrayOf(),
    override val nsfw : Boolean = false,
    override val rateLimitPerUser : Number = 0,
) : GuildChannel, TextBasedChannel  {
    override suspend fun sendMessage(payload: MessageOptions) {
        client.user.sendMessage(this, payload)
    }
}