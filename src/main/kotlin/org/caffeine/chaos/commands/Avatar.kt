package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Avatar : Command(arrayOf("av", "avatar", "pfp")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            if (args.isNotEmpty() && event.message.mentions.isEmpty()) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: '${args.joinToString(" ")}}' is not a mentioned user.")
                        .appendLine("Correct usage: `${client.config.prefix}info @user`")
                        .build()
                ).thenAccept { message ->
                    this.launch { onComplete(message, client, true) }
                }
                return@coroutineScope
            }

            val userName: String
            val avatarURL: String

            if (args.isEmpty()) {
                userName = client.user.discriminatedName
                avatarURL = client.user.avatarUrl()
            } else {
                val user = event.message.mentions.first()
                userName = user.discriminatedName
                avatarURL = user.avatarUrl()
            }

            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("${userName}'s Avatar")
                    .appendLine(avatarURL)
                    .build()
            )
                .thenAccept { this.launch { onComplete(it, client, client.config.auto_delete.bot.content_generation) } }
        }
}