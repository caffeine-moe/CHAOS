package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.entities.asSnowflake
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen

class IdToDate : Command(
    arrayOf("idtodate", "idtod", "idtd"),
    CommandInfo("IdToDate", "idtodate <Discord ID>", "Converts any discord id to a normal date format.")
) {
    override suspend fun onCalled(
        client: Client,
        event: ClientEvent.MessageCreate,
        args: List<String>,
        cmd: String,
    ) {
        var id: Long = 0
        val err = if (args.isEmpty()) {
            "Not enough arguments."
        } else {
            try {
                id = args.first().toLong()
                ""
            } catch (_: NumberFormatException) {
                "'${args.joinToString(" ")}' is not a discord id."
            }
        }
        if (err.isNotEmpty()) {
            event.channel.sendMessage(
                error(client, event, err, commandInfo)
            ).awaitThen {
                onComplete(it, true)
            }
            return
        }
        val simpleDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = java.util.Date(id.toString().asSnowflake().timestamp.toEpochMilliseconds())
        val formattedDate = simpleDateFormat.format(date)
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("**The id $id as a date is:**")
                .appendLine(formattedDate)
        ).awaitThen {
            onComplete(it, true)
        }
    }
}