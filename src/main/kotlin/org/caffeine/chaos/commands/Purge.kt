package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.client.message.MessageFilters
import org.caffeine.chaos.purgeCock

class Purge : Command(arrayOf("purge", "sclear"),
    CommandInfo("Purge", "purge <Amount>", "Deletes a specified amount of YOUR messages from a channel.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
            if (args.isEmpty()) {
                event.channel.sendMessage(error(client, event, "Not enough parameters.", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            purgeCock = false
            if (args.last().toString().contains("[^0-9]".toRegex())) {
                event.channel.sendMessage(error(client, event, "${args.last()} is not an integer.", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            val num = args.last().toInt()
            if (num <= 0) {
                event.channel.sendMessage(error(client, event, "Amount must be higher than 0.", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            var done = 0
            val messages =
                event.channel.messagesAsCollection(MessageFilters(author_id = client.user.id, needed = num))
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
            if (done > 1) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Removed $done messages!")
                    .build())
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
            }
            if (done == 1) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Removed $done message!")
                    .build())
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
            }

        }
}