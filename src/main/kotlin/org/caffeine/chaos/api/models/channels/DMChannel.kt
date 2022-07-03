package org.caffeine.chaos.api.models.channels

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.typedefs.ChannelType
import java.util.*

class DMChannel(
    override val id: String,
    override val client: Client,
    override val type: ChannelType,
    val lastMessageId: String,
    val lastPinTimestamp: Date,
    override val name: String,
    val position: Number,
) : BaseChannel