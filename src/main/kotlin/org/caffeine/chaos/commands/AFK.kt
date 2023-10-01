package org.caffeine.chaos.commands

import org.caffeine.chaos.config
import org.caffeine.chaos.handlers.afk
import org.caffeine.chaos.handlers.afkMessage
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.client.ClientImpl
import org.caffeine.octane.client.user.ClientUser
import org.caffeine.octane.client.user.CustomStatus
import org.caffeine.octane.typedefs.StatusType
import org.caffeine.octane.utils.log

var oldCustomStatus = CustomStatus()
var oldStatus = StatusType.DND

class AFK :
    Command(arrayOf("afk", "away"), CommandInfo("AFK", "afk [Message]", "Auto replies to mentions when you are afk.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (client.user !is ClientUser) return
        val cu = client.user as ClientUser
        val prefix = "AFK:"
        if (afk) return
        afkMessage = config.afk.message
        if (args.isNotEmpty()) {
            afkMessage = args.joinToString(" ")
        }
        oldStatus = cu.settings.status
        oldCustomStatus = cu.settings.customStatus
        client as ClientImpl
        val newStatus = StatusType.enumById(config.afk.status)
        if (newStatus == StatusType.UNKNOWN) {
            log("Invalid status type: ${config.afk.status}", prefix)
            log("Using default status: ${StatusType.DND}", prefix)
            cu.setStatus(StatusType.DND)
        }
        cu.setStatus(newStatus)
        cu.setCustomStatus(CustomStatus(text = afkMessage))
        afk = afk.not()
        log("Set AFK to $afk", prefix)
    }
}