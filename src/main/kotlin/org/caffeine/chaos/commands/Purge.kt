package org.caffeine.chaos.commands

import kotlinx.coroutines.delay
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.models.message.MessageFilters
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.purgeCock

class Purge : Command(
    arrayOf("purge", "sclear"),
    CommandInfo("Purge", "purge [Channel] <Amount>", "Deletes a specified amount of YOUR messages from a channel.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        purgeCock = false
        val channel = when {
            args.size < 1 -> {
                event.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                    .await().also { message -> onComplete(message, true) }
                return
            }

            args.size == 1 -> {
                event.channel
            }

            args.size == 2 -> {
                val channel = client.user.fetchChannelFromId(args[1]) as TextBasedChannel
                channel
            }

            else -> {
                event.channel.sendMessage(error(client, event, "Too many arguments.", commandInfo))
                    .await().also { message -> onComplete(message, true) }
                return
            }
        }
        val num = if (!args.last().toString().contains("[^0-9]".toRegex())) {
            if (args.last().toInt() <= 0) {
                event.channel.sendMessage(
                    error(
                        client,
                        event,
                        "Amount must be higher than 0.",
                        commandInfo
                    )
                )
                    .await().also { message -> onComplete(message, true) }
                return
            }
            args.last().toInt()
        } else {
            when (args.last()) {
                "max" -> {
                    0
                }

                "all" -> {
                    0
                }

                else -> {
                    event.message.channel.sendMessage(
                        error(
                            client,
                            event,
                            "${args.last()} is not an integer.",
                            commandInfo
                        )
                    )
                        .await().also { message -> onComplete(message, true) }
                    return
                }
            }
        }
        var done = 0
        val progress = event.channel.sendMessage("Fetching messages...").await()
        val messages  =
            channel.fetchHistory(MessageFilters(author_id = client.user.id, needed = num)).filter { message ->
                message.author.id == client.user.id && message.type == MessageType.DEFAULT || message.type == MessageType.REPLY
            }
        if (messages.isEmpty()) {
            progress.edit("There is nothing to delete!")
                .await().also { message -> onComplete(message, true) }
            return
        }
        for (message : Message in messages) {
            if (done % 10 == 0 && done != 0) delay(5000)
            message.delete()
            done++
            progress.edit("Deleted message $done/${messages.size}...").await()
            delay(1000)
            if (!purgeCock || done != num) continue
            break
        }
        progress.edit("Removed $done message${if (done > 1) "s" else ""}!")
            .await().also { message -> onComplete(message, true) }
    }
}