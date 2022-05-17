package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.*

var commandList: HashMap<String, Command> = HashMap()

var spamCock = false
var purgeCock = false

fun registerCommands() {
    Token()
    Help()
    Ping()
    IP()
    Avatar()
    Spam()
    SSpam()
    Purge()
    SPurge()
    Backup()
    Restore()
    //exchange(client, event)
    Coin()
    Clear()
    SelfDestruct()
    LeaveGroupDms()
    Status()
    Figlet()
    Upload()
    Cat()
    Haste()
    CloseDm()
    Uptime()
    SysFetch()
    UserInfo()
}

suspend fun handleMessage(event: MessageCreateEvent, client: Client) {
    if (client.config.nitro_sniper.enabled && client.user.verified) {
        nitroSniper(event, client)
    }

    if (event.message.author.id == client.user.id) {

        if (event.message.content.startsWith(client.config.prefix) && event.message.content != client.config.prefix) {
            val commandName: String
            try {
                commandName =
                    event.message.content.lowercase().replaceFirst(client.config.prefix, "").split(" ").first()
            } catch (e: Exception) {
                return
            }

            val command: Command = commandList.get(commandName) ?: return

            if (client.config.logger.commands) {
                log(event.message.content, "COMMAND:\u001B[38;5;33m")
            }

            if (client.config.auto_delete.user.enabled) {
                user(event, client)
            }

            val args = event.message.content.split(" ").toMutableList()
            args.removeAt(0)

            command.onCalled(client, event, args, commandName)
        }

        if (client.config.anti_scam.enabled) {
            antiScam(client, event)
        }
    }
}
