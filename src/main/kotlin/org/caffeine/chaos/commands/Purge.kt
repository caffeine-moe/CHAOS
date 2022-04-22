package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.Message
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config

private var cock = false

suspend fun purge(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == "${config.prefix}purge" || event.message.content.lowercase() == "${config.prefix}sclear") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage:** '${event.message.content}'")
            .appendLine("**Error:** Not enough parameters!")
            .appendLine("**Correct usage:** `${config.prefix}purge Int`")
            .build(), config, client)
            .thenAccept { message -> this.launch { bot(message, config) } }
    }
    if (event.message.content.lowercase().startsWith("${config.prefix}purge ") || event.message.content.lowercase()
            .startsWith("${config.prefix}sclear ") && event.message.content.lowercase() != "${config.prefix}purge" && event.message.content.lowercase() != "${config.prefix}sclear"
    ) {
        cock = false
        val number = event.message.content.lowercase().replace("[^0-9]".toRegex(), "")
        try {
            val num = event.message.content.lowercase().replace("[^0-9]".toRegex(), "").toInt()
            if (num <= 0) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("**Incorrect usage:** '${event.message.content}'")
                        .appendLine("**Error:** Int must be higher than 0!")
                        .appendLine("**Correct usage:** `${config.prefix}purge Int`")
                        .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
                return@coroutineScope
            }
            var done = 0
            val count = event.channel.messagesAsStream(config).filter { x -> x.author == event.message.author }.count()
            if (count <= 0) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("There is nothing to delete!")
                        .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
                return@coroutineScope
            }
            for (message: Message in event.channel.messagesAsStream(config)
                .filter { x -> x.author == event.message.author }) {
                if (done % 8 == 0 && done != 0) {
                    withContext(Dispatchers.IO) {
                        Thread.sleep(4500)
                    }
                }
                message.delete(config)
                withContext(Dispatchers.IO) {
                    Thread.sleep(500)
                }
                done++
                if (cock) break
                if (done == num) break
            }
            if (done > 1) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Removed $done messages!")
                    .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
            }
            if (done == 1) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Removed $done message!")
                    .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
            }
        } catch (e: Exception) {
            when (e) {
                is NumberFormatException -> {
                    event.channel.sendMessage(
                        MessageBuilder()
                            .appendLine("**Incorrect usage:** '${event.message.content}'")
                            .appendLine("**Error:** $number is not an integer!")
                            .appendLine("**Correct usage:** `${config.prefix}purge Int`")
                            .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                    return@coroutineScope
                }
                else -> {
                    println("purge")
                    println(e)
                }
            }
        }
    }
}

suspend fun sPurge(client: Client, event: MessageCreateEvent, config: Config) {
    if (event.message.content.lowercase() == "${config.prefix}spurge") {
        cock = true
    }
}