package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.ConsoleColours
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.commands.*

var commandList : HashMap<String, Command> = HashMap()

// spam stopper, if true, stops the message spammer
var spamCock = false

// purge stopper, if true, stops the message purger
var purgeCock = false

// autobump stopper, if true, stops autobump
var autoBumpCock = false

// var bumping = mutableListOf<MessageChannel>()

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
    // exchange(client, event)
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
    // AutoBump()
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

suspend fun handleMessage(event : ClientEvent.MessageCreate, client : Client) {
    if (config.nitro_sniper.enabled && client.user.verified) nitroSniper(event, client)

    if (afk) afkHandler(event, client)

    if (event.message.author.id != client.user.id) {
        if (config.cdnpls.enabled) cdnpls(event)
        if (config.anti_scam.enabled) antiScam(client, event)
    }

    if (event.message.content.startsWith(config.prefix) && event.message.content != config.prefix) {
        val commandName : String =
            event.message.content.lowercase().replaceFirst(config.prefix, "").split(" ").first()

        val command : Command = commandList[commandName] ?: return

        if (config.logger.commands) log(event.message.content, "COMMAND:${ConsoleColours.BLUE.value}")

        if (config.auto_delete.user.enabled) user(event)

        val args = event.message.content.lowercase().replaceFirst(config.prefix, "").split(" ").toMutableList()
        args.removeAt(0)

        command.onCalled(client, event, args, commandName)
    }
}
