package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.commands.*

//HashMap of commands
var commandList : HashMap<String, Command> = HashMap()

//spam stopper, if true, stops the message spammer
var spamCock = false

//purge stopper, if true, stops the message purger
var purgeCock = false

//autobump stopper, if true, stops autobump
var autoBumpCock = false

//list of channels that autobump is currently bumping
//var bumping = mutableListOf<MessageChannel>()

//list of all guilds that the client is lazy loading
//private val guilds = HashMap<String, ClientGuild>()

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
    //AutoBump()
    MuteAllServers()
    Theme()
    Pin()
    HypeSquad()
    Dice()
    IdToDate()
    Update()
    AFK()
    RandomChoice()
    GayPFP()
    ChannelName()
}

//executed whenever a message event is received by the client
suspend fun handleMessage(event : ClientEvents.MessageCreate, client : Client) {
    //if nitro sniper is enabled and email is verified, pass the message to the nitro sniper
    if (config.nitro_sniper.enabled && client.user.verified) {
        nitroSniper(event, client)
    }

    if (afk) {
        afkHandler(event, client)
    }

    //if the message is sent by the user the selfbot is logged into then do stuff
    if (event.message.author.id == client.user.id) {

/*        val guild = client.user.getGuild(event.channel)

    `    if (guild != null) {
            if (!guilds.contains(guild.id)) {
                client.user.fetchGuildMembers(guild)
                guilds[guild.id] = guild
            }
        }*/

        //if the message starts with the configured prefix and isn't just the prefix
        //then remove the prefix and set the first item in the message (the command) as a value
        if (event.message.content.startsWith(config.prefix) && event.message.content != config.prefix) {
            val commandName : String =
                event.message.content.lowercase().replaceFirst(config.prefix, "").split(" ").first()
            //creates a new command object and
            //checks if the first item in the message (commandName) is a command and matches it to the command in the HashMap commandList.
            //if it can't, return
            val command : Command = commandList[commandName] ?: return

            //if the command logger is enabled then log the command
            if (config.logger.commands) {
                log(event.message.content, "COMMAND:${ConsoleColours.BLUE.value}")
            }

            //if autodelete on the user is enabled then delete the command message
            if (config.auto_delete.user.enabled) {
                user(event)
            }

            //set args value as the message content split by spaces into a list
            //and removes the first element (the actual command)
            val args = event.message.content.lowercase().replaceFirst(config.prefix, "").split(" ").toMutableList()
            args.removeAt(0)

            //finally, execute the command
            command.onCalled(client, event, args, commandName)
        }
        if (config.cdnpls.enabled) {
            cdnpls(event)
        }

    }

    //if all else fails
    //if anti scam is enabled in config then pass the message through the anti scam checker
    if (config.anti_scam.enabled) {
        antiScam(client, event)
    }

}