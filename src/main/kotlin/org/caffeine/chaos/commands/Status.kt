package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientStatusType
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Status : Command(arrayOf("status", "st"), CommandInfo("status <Status>", "Changes your current status.")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        var err = if (args.isEmpty()) "No status specified." else ""
        if (args.isNotEmpty()) {
            when (args.first()) {
                "online" -> client.user.setStatus(ClientStatusType.ONLINE).also { return@coroutineScope }
                "idle", "away" -> client.user.setStatus(ClientStatusType.IDLE).also { return@coroutineScope }
                "dnd", "donotdisturb" -> client.user.setStatus(ClientStatusType.DND).also { return@coroutineScope }
                "offline", "invis", "invisible" -> client.user.setStatus(ClientStatusType.INVISIBLE)
                    .also { return@coroutineScope }
                else -> {
                    err = "Invalid status ${args.joinToString(" ")}."
                }
            }
        }
        event.channel.sendMessage(error(client, event, err, commandInfo))
    }
}