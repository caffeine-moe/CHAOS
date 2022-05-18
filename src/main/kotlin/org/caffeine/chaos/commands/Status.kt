package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientStatusType
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Status : Command(arrayOf("online", "idle", "away", "dnd", "donotdisturb", "offline", "invis", "invisible")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) {
        when (cmd) {
            "online" -> client.user.setStatus(ClientStatusType.ONLINE)
            "idle", "away" -> client.user.setStatus(ClientStatusType.IDLE)
            "dnd", "donotdisturb" -> client.user.setStatus(ClientStatusType.DND)
            "offline", "invis", "invisible" -> client.user.setStatus(ClientStatusType.INVISIBLE)
        }
    }
}