package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.*

suspend fun commandHandler(event: MessageCreateEvent, client: Client) {
    if (client.config.nitro_sniper.enabled && client.user.verified) {
        nitroSniper(event, client)
    }
    if (event.message.author?.id == client.user.id) {
        if (event.message.content.startsWith(client.config.prefix) && event.message.content != client.config.prefix) {
            val toFind = event.message.content.replaceFirst(client.config.prefix, "", true)
            val found = commandlist.any { cmd -> toFind == cmd || toFind.startsWith("$cmd ") }
            if (found) {
                if (client.config.logger.commands) {
                    log(event.message.content, "COMMAND:\u001B[38;5;33m")
                }
                if (client.config.auto_delete.user.enabled) {
                    user(event, client)
                }
                token(client, event)
                help(client, event)
                ping(client, event)
                ip(client, event)
                avatar(client, event)
                spam(client, event)
                sSpam(client, event)
                purge(client, event)
                sPurge(client, event)
                backup(client, event)
                //Exchange(client, event, config)
                coin(client, event)
                clear(client, event)
                selfDestruct(client, event)
                lgdm(client, event)
                online(client, event)
                away(client, event)
                dnd(client, event)
                invis(client, event)
            }
        }
    }
}
