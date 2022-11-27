package org.caffeine.chaos.api.entities.channels

import org.caffeine.chaos.api.Snowflake
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.guild.Guild
import org.caffeine.chaos.api.entities.guild.GuildImpl
import org.caffeine.chaos.api.typedefs.ChannelType
import java.util.*

interface GuildChannel : BaseChannel {
    val guild : Guild
    val position : Number
    val permissionOverwrites : Array<Any>
}
