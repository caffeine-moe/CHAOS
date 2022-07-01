package org.caffeine.chaos.commands

import org.caffeine.chaos.*
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientStatusType
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.log

var oldCustomStatus = ""
var oldStatus = ClientStatusType.DND

class AFK :
    Command(arrayOf("afk", "away"), CommandInfo("AFK", "afk [Message]", "Auto replies to mentions when you are afk.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) {
        val prefix = "AFK:"
        if (!afk) {
            afkMessage = config.afk.message
            if (args.isNotEmpty()) {
                afkMessage = args.joinToString(" ")
            }
            oldStatus = when (client.user.status.lowercase()) {
                "online" -> ClientStatusType.ONLINE
                "idle", "away" -> ClientStatusType.IDLE
                "dnd", "donotdisturb" -> ClientStatusType.DND
                "offline", "invis", "invisible" -> ClientStatusType.INVISIBLE
                else -> {
                    ClientStatusType.DND
                }
            }
            oldCustomStatus = client.user.customStatus.text
            when (config.afk.status.lowercase()) {
                "online" -> client.user.setStatus(ClientStatusType.ONLINE)
                "idle", "away" -> client.user.setStatus(ClientStatusType.IDLE)
                "dnd", "donotdisturb" -> client.user.setStatus(ClientStatusType.DND)
                "offline", "invis", "invisible" -> client.user.setStatus(ClientStatusType.INVISIBLE)
                else -> {
                    if (config.afk.status.isNotBlank()) {
                        log("Invalid status '${config.afk.status}'. Defaulting to DND.", prefix)
                        client.user.setStatus(ClientStatusType.DND)
                    }
                }
            }
            client.user.setCustomStatus(afkMessage)
            afk = afk.not()
            log("Set AFK to $afk", prefix)
        }
    }
}