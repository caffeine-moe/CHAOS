package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.typedefs.StatusType


class Status :
    Command(arrayOf("status", "st"), CommandInfo("Status", "status <Status>", "Changes your current status.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
        val err = if (args.isEmpty()) {
            "No status specified."
        } else {
            val status = client.utils.getStatusType(args[0])
            if (status != StatusType.UNKNOWN) {
                client.user.setStatus(status).also { return@coroutineScope }
            }
            "Invalid status '${args.joinToString(" ")}'."
        }
        //event.channel.sendMessage(error(client, event, err, commandInfo))
    }
}