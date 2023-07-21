package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.client.user.CustomStatus
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config
import org.caffeine.chaos.handlers.afk
import org.caffeine.chaos.handlers.afkMessage

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
        if (newStatus != oldStatus) {
            if (newStatus != StatusType.UNKNOWN) {
                cu.setStatus(newStatus)
            } else {
                log("Invalid status type: ${config.afk.status}", prefix)
                log("Using default status: ${StatusType.DND}", prefix)
                cu.setStatus(StatusType.DND)
            }
        }
        cu.setCustomStatus(CustomStatus(text = afkMessage))
        afk = afk.not()
        log("Set AFK to $afk", prefix)
    }
}