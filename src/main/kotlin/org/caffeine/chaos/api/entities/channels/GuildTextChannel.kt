package org.caffeine.chaos.api.entities.channels

import org.caffeine.chaos.api.Snowflake

interface GuildTextChannel : TextBasedChannel, GuildChannel {
    val parentId : Snowflake
    val topic : String
    val nsfw : Boolean
    val rateLimitPerUser : Number
}