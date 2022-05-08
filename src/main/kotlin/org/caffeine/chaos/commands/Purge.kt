package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.client.message.MessageFilters

private var cock = false

suspend fun purge(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}purge" || event.message.content.lowercase() == "${client.config.prefix}sclear") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage:** '${event.message.content}'")
            .appendLine("**Error:** Not enough parameters!")
            .appendLine("**Correct usage:** `${client.config.prefix}purge Int`")
            .build(), client)
            .thenAccept { message -> this.launch { onComplete(message, client) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}purge ") || event.message.content.lowercase()
            .startsWith("${client.config.prefix}sclear ") && event.message.content.lowercase() != "${client.config.prefix}purge" && event.message.content.lowercase() != "${client.config.prefix}sclear"
    ) {
        cock = false
        val split = event.message.content.split(" ").drop(1)
        if (split.last().toString().contains("[^0-9]".toRegex())) {
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**Incorrect usage:** '${event.message.content}'")
                    .appendLine("**Error:** ${split.last()} is not an integer!")
                    .appendLine("**Correct usage:** `${client.config.prefix}purge Int`")
                    .build(), client)
                .thenAccept { message -> this.launch { onComplete(message, client) } }
            return@coroutineScope
        }
        try {
            val num = split.last().toString().replace("[^0-9]".toRegex(), "").toInt()
            if (num <= 0) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("**Incorrect usage:** '${event.message.content}'")
                        .appendLine("**Error:** Int must be higher than 0!")
                        .appendLine("**Correct usage:** `${client.config.prefix}purge Int`")
                        .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client) } }
                return@coroutineScope
            }
            var done = 0
            val messages = event.channel.messagesAsCollection(MessageFilters(author_id = client.user.id, needed = num))
            if (messages.isEmpty()) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There is nothing to delete!")
                        .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client) } }
                return@coroutineScope
            }
            for (message: Message in messages.filter { message -> message.author.id == client.user.id }) {
                if (message.type != 3) {
                    if (done % 10 == 0 && done != 0) {
                        withContext(Dispatchers.IO) {
                            Thread.sleep(5000)
                        }
                    }
                    message.delete(client)
                    withContext(Dispatchers.IO) {
                        Thread.sleep(1000)
                    }
                    done++
                    if (cock) break
                    if (done == num) break
                }
            }
            if (done < 1) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There is nothing to delete!")
                        .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client) } }
                return@coroutineScope
            }
            if (done > 1) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Removed $done messages!")
                    .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client) } }
            }
            if (done == 1) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Removed $done message!")
                    .build(), client)
                    .thenAccept { message -> this.launch { onComplete(message, client) } }
            }
        } catch (e: Exception) {
            when (e) {
                is NoSuchElementException -> {

                }
                else -> {
                    println("purge")
                    println(e)
                }
            }
        }
    }
}

fun sPurge(client: Client, event: MessageCreateEvent) {
    if (event.message.content.lowercase() == "${client.config.prefix}spurge") {
        cock = true
    }
}