package org.caffeine.chaos.api.entities.channels

import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.typedefs.ChannelType

class ChannelCategoryImpl(
    override val client : Client,
    override val id : Snowflake,
    override val name : String,
    override val type : ChannelType,
    override val position : Int,
) : ChannelCategory {

    var guildId : Snowflake = Snowflake(0)

    override var guild : Guild
        get() = runBlocking(client.coroutineContext) { client.user.fetchGuild(guildId) }
        set(value) {
            guildId = value.id
        }

    override suspend fun delete() {
        client.user.deleteChannel(this)
    }
}