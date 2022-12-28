package org.caffeine.chaos.api.entities.channels

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.typedefs.ChannelType

interface BaseChannel {
    suspend fun delete()

    val client : Client
    val id : Snowflake
    val name : String
    val type : ChannelType
}
