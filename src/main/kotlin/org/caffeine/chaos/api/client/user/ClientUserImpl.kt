package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.models.guild.Guild
import org.caffeine.chaos.api.models.interfaces.BaseChannel
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.models.message.MessageSearchFilters
import kotlin.math.absoluteValue

data class ClientUserImpl(
    override var verified : Boolean,
    override var username : String,
    override var discriminator : String,
    override var id : String,
    override var email : String?,
    override var bio : String?,
    override var settings : ClientUserSettings,
    override var avatar : String?,
    override var relationships : ClientUserRelationships,
    override var premium : Boolean,
    override var token : String,
    override val bot : Boolean,
    override val client : Client,
    override val clientImpl : ClientImpl,
) : BaseClientUserImpl {

    override val discriminatedName : String
        get() = "$username#$discriminator"


    override suspend fun fetchLastMessageInChannel(
        channel : TextBasedChannel,
        filters : MessageSearchFilters,
    ) : Message? {
        return clientImpl.utils.fetchLastMessageInChannel(channel, this, filters)
    }

    override fun avatarUrl() : String {
        return if (!avatar.isNullOrBlank()) {
            if (avatar!!.startsWith("a_")) {
                "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/$id/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }

    var _channels = HashMap<String, BaseChannel>()
    override val channels : Map<String, BaseChannel>
        get() = _channels

    var _guilds = HashMap<String, Guild>()
    override val guilds : Map<String, Guild>
        get() = _guilds


    override suspend fun fetchMessagesFromChannel(
        channel : TextBasedChannel,
        filters : MessageFilters,
    ) : List<Message> {
        return clientImpl.utils.fetchMessages(channel, filters)
    }

    override suspend fun fetchChannelFromId(id : String) : BaseChannel? {
        return this._channels[id]
    }

}