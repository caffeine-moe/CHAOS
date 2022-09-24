package org.caffeine.chaos.commands

import org.caffeine.chaos.*
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.StatusType
import org.caffeine.chaos.api.utils.log

var oldCustomStatus = ""
var oldStatus = StatusType.DND

class AFK :
    Command(arrayOf("afk", "away"), CommandInfo("AFK", "afk [Message]", "Auto replies to mentions when you are afk.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val prefix = "AFK:"
        if (!afk) {
            afkMessage = config.afk.message
            if (args.isNotEmpty()) {
                afkMessage = args.joinToString(" ")
            }
            oldStatus = client.user.settings.status
            oldCustomStatus = client.user.settings.customStatus.text
            val newStatus = StatusType.DND//client.utils.getStatusType(config.afk.status)
            if (newStatus != oldStatus) {
                if (newStatus != StatusType.UNKNOWN) {
                    client.user.setStatus(newStatus)
                } else {
                    log("Invalid status type: ${config.afk.status}", prefix)
                    log("Using default status: ${StatusType.DND}", prefix)
                    client.user.setStatus(StatusType.DND)
                }
            }
            client.user.setCustomStatus(afkMessage)
            afk = afk.not()
            log("Set AFK to $afk", prefix)
        }
    }
}