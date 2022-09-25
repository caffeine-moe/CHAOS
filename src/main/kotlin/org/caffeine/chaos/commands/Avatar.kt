package org.caffeine.chaos.commands

import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.config

class Avatar : Command(
    arrayOf("avatar", "pfp", "av"),
    CommandInfo("Avatar", "av [@user]", "Sends your avatar or a mentioned users avatar.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isNotEmpty() && event.message.mentions.isEmpty()) {
            event.message.channel.sendMessage(
                error(
                    client,
                    event,
                    "'${args.joinToString(" ")}}' is not a mentioned user.",
                    commandInfo
                )
            ).await().also { message ->
                onComplete(message, true)
            }
            return
        }

        val user : DiscordUser
        val avatarURL : String

        if (args.isEmpty()) {
            user = client.user
            avatarURL = client.user.avatarUrl()
        } else {
            user = event.message.mentions.values.first()
            avatarURL = user.avatarUrl()
        }

        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("${user.discriminatedName}'s Avatar")
                .appendLine(avatarURL)
                .build()
        )
            .await().also { onComplete(it, config.auto_delete.bot.content_generation) }

    }
}
