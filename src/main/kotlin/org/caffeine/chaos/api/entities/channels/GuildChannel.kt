package org.caffeine.chaos.api.entities.channels

import org.caffeine.chaos.api.entities.guild.Guild

interface GuildChannel : BaseChannel {
    val guild : Guild
    val position : Number
    val permissionOverwrites : Array<Any>
}
