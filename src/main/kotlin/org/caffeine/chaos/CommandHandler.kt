package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.*
import org.caffeine.chaos.config.Config

suspend fun commandHandler(config: Config, event: MessageCreateEvent, client: Client) {
    if (config.nitro_sniper.enabled && client.user.verified) {
        nitroSniper(event, config, client)
    }
    if (event.message.author?.id == client.user.id) {
        if (event.message.content.startsWith(config.prefix) && event.message.content != config.prefix) {
            val toFind = event.message.content.replaceFirst(config.prefix, "", true)
            val found = commandlist.any { cmd -> toFind == cmd || toFind.startsWith("$cmd ") }
            if (found) {
                if (config.logger.commands) {
                    log(event.message.content, "COMMAND:\u001B[38;5;33m")
                }
                if (config.auto_delete.user.enabled) {
                    user(event, config)
                }
                token(client, event, config)
                help(client, event, config)
                ping(client, event, config)
                ip(client, event, config)
                avatar(client, event, config)
                spam(client, event, config)
                sSpam(event, config)
                purge(client, event, config)
                sPurge(client, event, config)
                backup(client, event, config)
                //Exchange(client, event, config)
                coin(client, event, config)
                clear(client, event, config)
                selfDestruct(client, event, config)
                lgdm(client, event, config)
                online(client, event, config)
                away(client, event, config)
                dnd(client, event, config)
                invis(client, event, config)
            }
        }
    }
}
