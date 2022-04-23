package org.caffeine.chaos.commands

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.config.Config

private var cock = false

suspend fun spam(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    cock = false
    if (event.message.content.lowercase() == "${config.prefix}spam") {
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("**Incorrect usage:** '${event.message.content}'")
                .appendLine("**Error:** Not enough parameters!")
                .appendLine("**Correct usage:** `${config.prefix}spam String Int`")
                .build(), config, client)
            .thenAccept { message -> this.launch { bot(message, config) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${config.prefix}spam ") && event.message.content.lowercase() != "${config.prefix}spam "
    ) {
        val msg = event.message.content.removePrefix("${config.prefix}spam ").split(" ")
        try {
            val number = msg[msg.lastIndex].replace("[^0-9]".toRegex(), "").toInt()
            val stringbuilder = StringBuilder()
            for (str: String in msg.dropLast(1)) {
                stringbuilder.append("$str ")
            }
            val string = stringbuilder.toString().removePrefix("${config.prefix}spam ").trim()
            if (number <= 0) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("**Incorrect usage:** '${event.message.content}'")
                        .appendLine("**Error:** Int must be higher than 0!")
                        .appendLine("**Correct usage:** `${config.prefix}spam String Int`")
                        .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
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
                event.channel.sendMessage(MessageBuilder().appendLine(string).build(), config, client)
                done++
                withContext(Dispatchers.IO) {
                    Thread.sleep(500)
                }
            }
            if (done > 1) {
                event.channel.sendMessage(MessageBuilder().appendLine("Done spamming '$string' $done times!")
                    .build(), config, client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
            }
            if (done == 1) {
                event.channel.sendMessage(MessageBuilder().appendLine("Done spamming '$string' once!").build(),
                    config,
                    client)
                    .thenAccept { message -> this.launch { bot(message, config) } }
            }
        } catch (e: Exception) {
            when (e) {
                is NumberFormatException -> {
                    event.channel.sendMessage(
                        MessageBuilder()
                            .appendLine("**Incorrect usage:** '${event.message.content}'")
                            .appendLine("**Error:** '${msg[msg.lastIndex]}' is not an integer!")
                            .appendLine("**Correct usage:** `${config.prefix}spam String Int`")
                            .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
                is IndexOutOfBoundsException -> {
                    event.channel.sendMessage(
                        MessageBuilder()
                            .appendLine("**Incorrect usage:** '${event.message.content}'")
                            .appendLine("**Error:** Error: Not enough parameters!")
                            .appendLine("**Correct usage:** `${config.prefix}spam String Int`")
                            .build(), config, client)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
            }
        }
    }
}

suspend fun sSpam(event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == "${config.prefix}sspam") {
        cock = true
    }
}
