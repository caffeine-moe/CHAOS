package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.commands.*

var commandList: HashMap<String, Command> = HashMap()

var spamCock = false
var purgeCock = false

//loads all the commands into the hashmap commandList
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

//executed whenever a message event is received by the client
suspend fun handleMessage(event: MessageCreateEvent, client: Client) {
    //if nitro sniper is enabled and email is verified, pass the message to the nitro sniper
    if (client.config.nitro_sniper.enabled && client.user.verified) {
        nitroSniper(event, client)
    }

    //if the message is sent by the user the selfbot is logged into then do stuff
    if (event.message.author.id == client.user.id) {
        //if the message starts with the configured prefix and isn't just the prefix
        //then remove the prefix and set the first item in the message (the command) as a value
        if (event.message.content.startsWith(client.config.prefix) && event.message.content != client.config.prefix) {
            val commandName: String
            try {
                commandName =
                    event.message.content.lowercase().replaceFirst(client.config.prefix, "").split(" ").first()
            } catch (e: Exception) {
                return
            }
            //creates a new command object and
            //checks if the first item in the message (commandName) is a command and matches it to the command in the HashMap commandList.
            //if it can't, return
            val command: Command = commandList.get(commandName) ?: return

            //if the command logger is enabled then log the command
            if (client.config.logger.commands) {
                log(event.message.content, "COMMAND:\u001B[38;5;33m")
            }

            //if autodelete on the user is enabled then delete the command message
            if (client.config.auto_delete.user.enabled) {
                user(event, client)
            }

            //set args value as the message content split by spaces into a list
            //and removes the first element (the actual command)
            val args = event.message.content.split(" ").toMutableList()
            args.removeAt(0)

            //finally, execute the command
            command.onCalled(client, event, args, commandName)
        }

    }

    //if all else fails
    //if anti scam is enabled in config then pass the message through the anti scam checker
    if (client.config.anti_scam.enabled) {
        antiScam(client, event)
    }

}