package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.*

var commandList2: HashMap<String, Command> = HashMap()

fun registerCommands() {
    TestCommand()
}

suspend fun handleMessage(event: MessageCreateEvent, client: Client) {
    if (event.message.author.id == client.user.id) {
        if (event.message.content.startsWith(client.config.prefix) && event.message.content != client.config.prefix) {
            var commandName: String = "";
            try {
                commandName = event.message.content.lowercase().replaceFirst(client.config.prefix, "").split(" ").first()
            } catch (e: Exception) {
                return
            }

            val command: Command = commandList2.get(commandName) ?: return

            if (client.config.auto_delete.user.enabled) {
                user(event, client)
            }

            val args = event.message.content.split(" ").toMutableList()
            args.removeAt(0)

            command.onCalled(client, event, args)
        }
    }
}

suspend fun messageHandler(event: MessageCreateEvent, client: Client) {
    if (client.config.nitro_sniper.enabled && client.user.verified) {
        nitroSniper(event, client)
    }
    if (event.message.author.id == client.user.id) {
        if (event.message.content.startsWith(client.config.prefix) && event.message.content != client.config.prefix) {
            val toFind = event.message.content.lowercase().replaceFirst(client.config.prefix, "")
            val found = commandList.any { cmd -> toFind == cmd || toFind.startsWith("$cmd ") }
            if (found) {
                if (client.config.auto_delete.user.enabled) {
                    user(event, client)
                }
                if (client.config.logger.commands) {
                    log(event.message.content, "COMMAND:\u001B[38;5;33m")
                }
                token(client, event)
                help(client, event)
                ping(client, event)
                ip(client, event)
//                avatar(client, event)
                spam(client, event)
                sSpam(client, event)
                purge(client, event)
                sPurge(client, event)
                backup(client, event)
                //exchange(client, event)
                coin(client, event)
                clear(client, event)
                selfDestruct(client, event)
                leaveGroupDM(client, event)
                online(client, event)
                away(client, event)
                dnd(client, event)
                invis(client, event)
                figlet(client, event)
                upload(client, event)
                cat(client, event)
                haste(client, event)
                closeDM(client, event)
                uptime(client, event)
                sysFetch(client, event)
                userInfo(client, event)
            }
        }
    }
    if (client.config.anti_scam.enabled) {
        antiScam(client, event)
    }
}
