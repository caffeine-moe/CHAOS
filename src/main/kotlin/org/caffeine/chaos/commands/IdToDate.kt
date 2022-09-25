package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.log

class IdToDate : Command(
    arrayOf("idtodate", "idtod", "idtd"),
    CommandInfo("IdToDate", "idtodate <Discord ID>", "Converts any discord id to a normal date format.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        var id : Long = 0
        val err = if (args.isEmpty()) {
            "Not enough arguments."
        } else {
            try {
                id = args.first().toLong()
                ""
            } catch (_ : NumberFormatException) {
                "'${args.joinToString(" ")}' is not a discord id."
            }
        }
        if (err.isEmpty()) {
            val simpleDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = java.util.Date(client.user.convertIdToUnix(id.toString()))
            val formattedDate = simpleDateFormat.format(date)
            log("$id converted to $formattedDate")
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**The id $id as a date is:**")
                    .appendLine(formattedDate)
                    .build()
            ).await().also {
                onComplete(it, true)
            }
            return
        }
        event.channel.sendMessage(
            error(client, event, err, commandInfo)
        ).await().also {
            onComplete(it, true)
        }
    }
}