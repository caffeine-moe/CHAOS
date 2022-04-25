package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun avatar(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}avatar" || event.message.content.lowercase() == "${client.config.prefix}av" && client.user.avatarUrl() != "") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("${client.user.discriminatedName}'s avatar")
            .appendLine(client.user.avatarUrl())
            .build(), client).thenAccept { message ->
            this.launch { bot(message, client) }
        }
    }
/*    if (event.message.content.lowercase()
            .startsWith("${config.prefix}av") && event.message.content.lowercase() != ("${config.prefix}av") || event.message.content.lowercase()
            .startsWith("${config.prefix}avatar") && event.message.content.lowercase() != ("${config.prefix}avatar")
    ) {
        if (event.message.mentionedUsers.isNotEmpty()) {
            val usr = event.message.mentionedUsers.first()
            event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("${usr.discriminatedName}'s Avatar")
                .appendLine(usr.)
                .build(), config, client)
        } else {
            event.message.channel.sendMessage(
                MessageBuilder()
                    .appendLine("Incorrect usage '${event.message.content}'")
                    .appendLine("Error: '${event.message.content.split(" ")[1]}' is not a mentioned user")
                    .appendLine("Correct usage: `${config.prefix}avatar @user`")
                    .build(), config, client).thenAccept { message ->
                this.launch { bot(message, config) }
            }
        }
    }*/
}