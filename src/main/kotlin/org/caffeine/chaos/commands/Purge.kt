package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.*
import org.caffeine.chaos.purgeCock

class Purge : Command(arrayOf("purge", "sclear"),
    CommandInfo("Purge", "purge [Channel] <Amount>", "Deletes a specified amount of YOUR messages from a channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
            purgeCock = false
            val channel = when {
                args.size < 1 -> {
                    event.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
                args.size == 1 -> {
                    event.channel
                }
                args.size == 2 -> {
                    if (!client.user.validateChannelId(args.first())) {
                        event.channel.sendMessage(error(client,
                            event,
                            "${args.first()} is not a valid channel.",
                            commandInfo))
                            .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                        return@coroutineScope
                    }
                    MessageChannel(args.first())
                }
                else -> {
                    event.channel.sendMessage(error(client, event, "Too many arguments.", commandInfo))
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
            }
            val num = if (!args.last().toString().contains("[^0-9]".toRegex())) {
                if (args.last().toInt() <= 0) {
                    event.channel.sendMessage(error(client, event, "Amount must be higher than 0.", commandInfo))
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    return@coroutineScope
                }
                args.last().toInt()
            } else {
                event.channel.sendMessage(error(client,
                    event,
                    "${args.last()} is not an integer.",
                    commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            var done = 0
            val messages =
                channel.messagesAsCollection(MessageFilters(author_id = client.user.id, needed = num))
            if (messages.isEmpty()) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There is nothing to delete!")
                        .build())
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            for (message : Message in messages.filter { message -> message.author.id == client.user.id }) {
                if (message.type != 3) {
                    if (done % 10 == 0 && done != 0) {
                        withContext(Dispatchers.IO) {
                            Thread.sleep(5000)
                        }
                    }
                    message.delete()
                    withContext(Dispatchers.IO) {
                        Thread.sleep(1000)
                    }
                    done++
                    if (purgeCock) break
                    if (done == num) break
                }
            }
            if (done < 1) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There is nothing to delete!")
                        .build())
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            event.channel.sendMessage(MessageBuilder()
                .appendLine("Removed $done message ${
                    if (done > 1) {
                        "s"
                    } else {
                        "1"
                    }
                }")
                .build())
                .thenAccept { message -> this.launch { onComplete(message, client, true) } }
        }


}