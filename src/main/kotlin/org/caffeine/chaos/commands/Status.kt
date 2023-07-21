package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.client.user.ClientUser
import org.caffeine.chaos.api.typedefs.StatusType

class Status :
    Command(arrayOf("status", "st"), CommandInfo("Status", "status <Status>", "Changes your current status.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val err = if (args.isEmpty()) {
            "No status specified."
        } else {
            val status = StatusType.valueOf(args[0])
            if (status != StatusType.UNKNOWN) {
                (client.user as ClientUser).setStatus(status).also { println("done"); return }
            }
            "Invalid status '${args.joinToString(" ")}'."
        }
        event.message.channel.sendMessage(error(client, event, err, commandInfo))
    }
}