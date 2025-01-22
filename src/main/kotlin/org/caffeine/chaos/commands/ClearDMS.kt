package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import java.util.*

class ClearDMS : Command(
    arrayOf("cdms"),
    CommandInfo("Clear DMs", "cdms", "Closes dm channels that you haven't spoken in in a month")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val oneMonthEarlier = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }.toInstant().toEpochMilli()
        client.user.dmChannels.values
            .filter { it.lastMessageId.timestamp.toEpochMilliseconds() <= oneMonthEarlier }
            .map { it.delete(); it.name }
            .ifEmpty { return }
            .also { onComplete("Deleted channels : ${it.joinToString(", ")}") }
    }
}