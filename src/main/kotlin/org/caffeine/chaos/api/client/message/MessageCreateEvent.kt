package org.caffeine.chaos.api.client.message

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.Event

data class MessageCreateEvent(
    var message : Message,
    var client : Client,
    var channel : MessageChannel,
) : Event