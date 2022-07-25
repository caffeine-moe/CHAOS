package org.caffeine.chaos.api.models.interfaces

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.typedefs.ChannelType

interface BaseChannel {
    val client : Client
    val id : String
    val name : String?
    val type : ChannelType
}