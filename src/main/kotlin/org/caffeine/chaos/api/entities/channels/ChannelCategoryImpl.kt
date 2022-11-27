package org.caffeine.chaos.api.entities.channels

import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.typedefs.ChannelType

class ChannelCategoryImpl(
    override val client : Client,
    override val id : Snowflake,
    override val name : String,
    override val type : ChannelType,
    override val position : Number,
    override val permissionOverwrites : Array<Any> = emptyArray()
) : ChannelCategory {

    var guildId : Snowflake = Snowflake("")

    override var guild : Guild
        get() = client.user.guilds[guildId] ?: throw Exception("FUCK YOU")
        set(value) {
            guildId = value.id
        }

    override suspend fun delete() {
        client.user.deleteChannel(this)
    }
}