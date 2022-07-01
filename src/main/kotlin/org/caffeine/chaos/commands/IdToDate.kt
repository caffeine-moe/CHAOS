package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.convertIdToUnix
import org.caffeine.chaos.api.utils.log

class IdToDate : Command(arrayOf("idtodate", "idtod", "idtd"),
    CommandInfo("IdToDate", "idtodate <Discord ID>", "Converts any discord id to a normal date format.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) = coroutineScope {
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
            val date = java.util.Date(convertIdToUnix(id.toString()))
            val formattedDate = simpleDateFormat.format(date)
            log("$id converted to $formattedDate")
/*            event.channel.sendMessage(MessageBuilder()
                .appendLine("**The id $id as a date is:**")
                .appendLine(formattedDate)
                .build()
            ).thenAccept {
                launch {
                    onComplete(it, client, true)
                }
            }
            return@coroutineScope
        }
        event.channel.sendMessage(
            error(client, event, err, commandInfo)
        ).thenAccept {
            launch {
                onComplete(it, client, true)
            }
        }*/
        }
    }
}