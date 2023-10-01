package org.caffeine.chaos.handlers

import org.caffeine.chaos.commands.Command
import org.caffeine.chaos.config
import org.caffeine.chaos.processes.autoDeleteUser
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.log
import kotlin.reflect.full.createInstance


var commandList : HashMap<String, Command> = HashMap()

// spam stopper, if true, stops the message spammer
var spamCock = false

// purge stopper, if true, stops the message purger
var purgeCock = false

// autobump stopper, if true, stops autobump
//var autoBumpCock = false

// var bumping = mutableListOf<MessageChannel>()

fun registerCommands() {
    val reflection = Command::class.sealedSubclasses
    reflection.forEach { it.createInstance() }
}

suspend fun handleCommand(event : ClientEvent.MessageCreate, client : Client) {
    if (!event.message.content.startsWith(config.prefix) || event.message.content == config.prefix) return

    val commandName : String =
        event.message.content.lowercase().replaceFirst(config.prefix.lowercase(), "").split(" ").first()

    val command : Command = commandList[commandName] ?: return

    if (config.logger.commands) log(
        event.message.content,
        "COMMAND:",
    )

    if (config.autoDelete.user.enabled) autoDeleteUser(event)

    val args = event.message.content.replaceFirst(config.prefix, "").split(" ").toMutableList()
    args.removeAt(0)

    command.onCalled(client, event, args, commandName)
}