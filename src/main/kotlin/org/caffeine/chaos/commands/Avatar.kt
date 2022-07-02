package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Avatar : Command(arrayOf("avatar", "pfp", "av"),
    CommandInfo("Avatar", "av [@user]", "Sends your avatar or a mentioned users avatar.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            if (args.isNotEmpty() && event.message.mentions.isEmpty()) {
                event.channel.sendMessage(error(client,
                    event,
                    "'${args.joinToString(" ")}}' is not a mentioned user.",
                    commandInfo)).thenAccept { message ->
                    this.launch { onComplete(message, client, true) }
                }
                return@coroutineScope
            }

            val user : DiscordUser
            val avatarURL : String

            if (args.isEmpty()) {
                user = client.user
                avatarURL = client.user.avatarUrl()
            } else {
                user = event.message.mentions.first()
                avatarURL = user.avatarUrl()
            }

            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("${user.discriminatedName}'s Avatar")
                    .appendLine(avatarURL)
                    .build()
            )
                .thenAccept { this.launch { onComplete(it, client, config.auto_delete.bot.content_generation) } }*/
        }
}