package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.typedefs.StatusType

class Status :
    Command(arrayOf("status", "st"), CommandInfo("Status", "status <Status>", "Changes your current status.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val err = if (args.isEmpty()) {
            "No status specified."
        } else {
            val status = StatusType.valueOf(args[0])
            if (status != StatusType.UNKNOWN) {
                client.user.setStatus(status).also { return }
            }
            "Invalid status '${args.joinToString(" ")}'."
        }
        event.message.channel.sendMessage(error(client, event, err, commandInfo))
    }
}
