package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.*

suspend fun commandHandler(config: Config, event: MessageCreateEvent, client: Client) {
    if (config.nitro_sniper.enabled) {
        NitroSniper(event, config)
    }
    if (event.message.author?.id == client.user.id) {
        if (event.message.content.startsWith(config.prefix) && event.message.content != config.prefix) {
            val toFind = event.message.content.replaceFirst(config.prefix, "", true)
            val found = commandlist.any { cmd -> toFind == cmd || toFind.startsWith("$cmd ") }
            if (found) {
                if (config.log_commands) {
                    log(event.message.content, "COMMAND:\u001B[38;5;33m")
                }
                if (config.auto_delete.user) {
                    user(event, config)
                }
                token(client, event, config)
                Help(client, event, config)
                Ping(client, event, config)
                IP(client, event, config)
                Avatar(client, event, config)
                Spam(client, event, config)
                SSpam(event, config)
                Purge(client, event, config)
                spurge(client, event, config)
                Backup(client, event, config)
                //Exchange(client, event, config)
                Coin(client, event, config)
                Clear(client, event, config)
                SelfDestruct(client, event, config)
                LGDM(client, event, config)
                online(client, event, config)
                away(client, event, config)
                dnd(client, event, config)
                invis(client, event, config)
            }
        }
    }
}
