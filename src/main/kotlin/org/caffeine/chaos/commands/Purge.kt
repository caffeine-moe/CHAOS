package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.interfaces.TextBasedChannel
import org.caffeine.chaos.api.models.message.Message
import org.caffeine.chaos.api.typedefs.MessageType
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.MessageFilters
import org.caffeine.chaos.api.utils.log
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
    ) =
        coroutineScope {
            purgeCock = false
            val channel = when {
                args.size < 1 -> {
                    event.message.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
                args.size == 1 -> {
                    event.message.channel
                }
                args.size == 2 -> {
                    val channel = client.utils.fetchChannel(args[1]) as TextBasedChannel
                    if (channel == null) {
                        event.message.channel.sendMessage(
                            error(
                                client,
                                event,
                                "${args.first()} is not a valid channel.",
                                commandInfo
                            )
                        )
                            .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                        return@coroutineScope
                    }
                    channel
                }
                else -> {
                    event.message.channel.sendMessage(error(client, event, "Too many arguments.", commandInfo))
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
            }
            val num = if (!args.last().toString().contains("[^0-9]".toRegex())) {
                if (args.last().toInt() <= 0) {
                    event.message.channel.sendMessage(
                        error(
                            client,
                            event,
                            "Amount must be higher than 0.",
                            commandInfo
                        )
                    )
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
                args.last().toInt()
            } else {
                when (args.last()) {
                    "max" -> {
                        Int.MAX_VALUE
                    }
                    "all" -> {
                        Int.MAX_VALUE
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
                            .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                        return@coroutineScope
                    }
                }
            }
            var done = 0
            val messages = channel.fetchHistory(MessageFilters(author_id = client.user.id, needed = num))
            if (messages.isEmpty()) {
                event.message.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There is nothing to delete!")
                        .build()
                )
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            for (message : Message in messages.filter { message -> message.author.id == client.user.id }) {
                if (message.type == MessageType.DEFAULT) {
                    if (done % 10 == 0 && done != 0) {
                        delay(5000)
                    }
                    message.delete()
                    delay(1000)
                    done++
                    if (purgeCock) break
                    if (done == num) break
                }
            }
            log("removed $done messages from ${channel.name}")
            event.message.channel.sendMessage(
                MessageBuilder()
                    .appendLine("Removed $done message${if (done > 1) "s" else ""}!")
                    .build()
            )
                .thenAccept { message -> this.launch { onComplete(message, client, true) } }
        }
}