package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Avatar : Command(arrayOf("av", "avatar")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>) = coroutineScope{
        if (args.isEmpty()){
            event.channel.sendMessage(MessageBuilder()
                .appendLine("${client.user.discriminatedName}'s Avatar")
                .appendLine(client.user.avatarUrl())
                .build())
                .thenAccept { this.launch { onComplete(it, client, client.config.auto_delete.bot.content_generation) } }
            return@coroutineScope
        }
        if (event.message.mentions.isNotEmpty()){
            event.channel.sendMessage(MessageBuilder()
                .appendLine("${event.message.mentions.first().discriminatedName}'s Avatar")
                .appendLine(event.message.mentions.first().avatarUrl())
                .build())
                .thenAccept { this.launch { onComplete(it, client, client.config.auto_delete.bot.content_generation) } }
        }
        if (args.isNotEmpty() && event.message.mentions.isEmpty()) {
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("Incorrect usage '${event.message.content}'")
                    .appendLine("Error: '${args.joinToString(" ")}}' is not a mentioned user.")
                    .appendLine("Correct usage: `${client.config.prefix}info @user`")
                    .build()).thenAccept { message ->
                this.launch { onComplete(message, client, true) }
            }
        }
    }
}