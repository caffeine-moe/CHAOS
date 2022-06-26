package org.caffeine.chaos.api.discord.message

import org.caffeine.chaos.api.discord.client.Client

data class MessageCreateEvent(
    var message : Message,
    var client : Client,
    var channel : MessageChannel,
)