package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

private var cock = false

suspend fun spam(client: Client, event: MessageCreateEvent) = coroutineScope {
    cock = false
    if (event.message.content.lowercase() == "${client.config.prefix}spam") {
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("**Incorrect usage:** '${event.message.content}'")
                .appendLine("**Error:** Not enough parameters!")
                .appendLine("**Correct usage:** `${client.config.prefix}spam String Int`")
                .build(), client)
            .thenAccept { message -> this.launch { bot(message, client) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}spam ") && event.message.content.lowercase() != "${client.config.prefix}spam "
    ) {
        val msg = event.message.content.removePrefix("${client.config.prefix}spam ").split(" ")
        try {
            val number = msg[msg.lastIndex].replace("[^0-9]".toRegex(), "").toInt()
            val stringbuilder = StringBuilder()
            for (str: String in msg.dropLast(1)) {
                stringbuilder.append("$str ")
            }
            val string = stringbuilder.toString().removePrefix("${client.config.prefix}spam ").trim()
            if (number <= 0) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("**Incorrect usage:** '${event.message.content}'")
                        .appendLine("**Error:** Int must be higher than 0!")
                        .appendLine("**Correct usage:** `${client.config.prefix}spam String Int`")
                        .build(), client)
                    .thenAccept { message -> this.launch { bot(message, client) } }
                return@coroutineScope
            }
            var done = 0
            while (done < number) {
                if (cock) {
                    break
                }
                if (done % 8 == 0 && done != 0) {
                    withContext(Dispatchers.IO) {
                        Thread.sleep(5000)
                    }
                }
                event.channel.sendMessage(MessageBuilder().appendLine(string).build(), client)
                done++
                withContext(Dispatchers.IO) {
                    Thread.sleep(350)
                }
            }
            if (done > 1) {
                event.channel.sendMessage(MessageBuilder().appendLine("Done spamming '$string' $done times!")
                    .build(), client)
                    .thenAccept { message -> this.launch { bot(message, client) } }
            }
            if (done == 1) {
                event.channel.sendMessage(MessageBuilder().appendLine("Done spamming '$string' once!").build(),
                    client)
                    .thenAccept { message -> this.launch { bot(message, client) } }
            }
        } catch (e: Exception) {
            when (e) {
                is NumberFormatException -> {
                    event.channel.sendMessage(
                        MessageBuilder()
                            .appendLine("**Incorrect usage:** '${event.message.content}'")
                            .appendLine("**Error:** '${msg[msg.lastIndex]}' is not an integer!")
                            .appendLine("**Correct usage:** `${client.config.prefix}spam String Int`")
                            .build(), client)
                        .thenAccept { message -> this.launch { bot(message, client) } }
                }
                is IndexOutOfBoundsException -> {
                    event.channel.sendMessage(
                        MessageBuilder()
                            .appendLine("**Incorrect usage:** '${event.message.content}'")
                            .appendLine("**Error:** Error: Not enough parameters!")
                            .appendLine("**Correct usage:** `${client.config.prefix}spam String Int`")
                            .build(), client)
                        .thenAccept { message -> this.launch { bot(message, client) } }
                }
            }
        }
    }
}

suspend fun sSpam(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}sspam") {
        cock = true
    }
}
