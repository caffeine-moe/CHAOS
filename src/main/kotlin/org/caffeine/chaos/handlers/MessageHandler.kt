package org.caffeine.chaos.handlers

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.config
import org.caffeine.chaos.processes.antiScam
import org.caffeine.chaos.processes.cdnpls
import org.caffeine.chaos.processes.nitroSniper

suspend fun handleMessage(event : ClientEvent.MessageCreate, client : Client) {
    if (afk) afkHandler(event, client)
    if (event.message.author.id == client.user.id) handleCommand(event, client)
    (client.user as? ClientUser)
        ?.let {
            if (config.nitroSniper.enabled && it.verified) nitroSniper(event, it)
        }
    if (config.cdnpls.enabled && event.message.author.id == client.user.id) cdnpls(event)
    if (config.antiScam.enabled) antiScam(client, event)
}