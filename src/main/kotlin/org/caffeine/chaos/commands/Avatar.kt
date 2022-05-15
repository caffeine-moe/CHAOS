package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun avatar(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content == "${client.config.prefix}av" || event.message.content == "${client.config.prefix}avatar") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("${client.user.discriminatedName}'s Avatar")
            .appendLine(client.user.avatarUrl())
            .build())
            .thenAccept { this.launch { onComplete(it, client, client.config.auto_delete.bot.content_generation) } }
    }
    if (event.message.content.startsWith("${client.config.prefix}av") && event.message.content.split(" ").size > 1 && !event.message.mentions.isNullOrEmpty() || event.message.content.startsWith(
            "${client.config.prefix}avatar") && event.message.content.split(" ").size > 1 && !event.message.mentions.isNullOrEmpty()
    ) {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("${event.message.mentions!!.first().discriminatedName}'s Avatar")
            .appendLine(event.message.mentions!!.first().avatarUrl())
            .build())
            .thenAccept { this.launch { onComplete(it, client, client.config.auto_delete.bot.content_generation) } }
    }
    if (event.message.content.startsWith("${client.config.prefix}av") && event.message.content.split(" ").size > 1 && event.message.mentions.isNullOrEmpty() || event.message.content.startsWith(
            "${client.config.prefix}avatar") && event.message.content.split(" ").size > 1 && event.message.mentions.isNullOrEmpty()
    ) {
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("Incorrect usage '${event.message.content}'")
                .appendLine("Error: '${
                    event.message.content.split(" ").drop(1).joinToString(" ")
                }' is not a mentioned user.")
                .appendLine("Correct usage: `${client.config.prefix}info @user`")
                .build()).thenAccept { message ->
            this.launch { onComplete(message, client, true) }
            return@thenAccept
        }
    }
}