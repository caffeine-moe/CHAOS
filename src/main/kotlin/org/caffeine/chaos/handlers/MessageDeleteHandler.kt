package org.caffeine.chaos.handlers

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config

fun handleMessageDelete(event : ClientEvent.MessageDelete, client : Client) {
    if (!config.logger.deletedMessages) return
    if (event.message.content.isBlank()) return
    if (event.message.author == client.user) return
    val guild = if (event.message.guild != null) {
        " Guild : \"${event.message.guild?.name}\""
    } else ""
    log(
        "\"${event.message.content}\" in$guild Channel : \"${event.message.channel.name}\"",
        "${event.message.author.username} DELETED:"
    )
}