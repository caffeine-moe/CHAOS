package org.caffeine.chaos.commands

import org.caffeine.chaos.afk
import org.caffeine.chaos.afkMessage
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.ClientImpl
import org.caffeine.chaos.api.client.user.CustomStatus
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.config

var oldCustomStatus = CustomStatus()
var oldStatus = StatusType.DND

class AFK :
    Command(arrayOf("afk", "away"), CommandInfo("AFK", "afk [Message]", "Auto replies to mentions when you are afk.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val prefix = "AFK:"
        if (afk) return
        afkMessage = config.afk.message
        if (args.isNotEmpty()) {
            afkMessage = args.joinToString(" ")
        }
        oldStatus = client.user.settings?.status ?: return
        oldCustomStatus = client.user.settings?.customStatus ?: return
        client as ClientImpl
        val newStatus = client.utils.getStatusType(config.afk.status)
        if (newStatus != oldStatus) {
            if (newStatus != StatusType.UNKNOWN) {
                client.user.setStatus(newStatus)
            } else {
                log("Invalid status type: ${config.afk.status}", prefix)
                log("Using default status: ${StatusType.DND}", prefix)
                client.user.setStatus(StatusType.DND)
            }
        }
        client.user.setCustomStatus(CustomStatus(text = afkMessage))
        afk = afk.not()
        log("Set AFK to $afk", prefix)
    }
}
