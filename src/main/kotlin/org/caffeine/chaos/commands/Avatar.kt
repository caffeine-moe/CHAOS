package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.config

class Avatar : Command(
    arrayOf("avatar", "pfp", "av"),
    CommandInfo("Avatar", "av [@autoDeleteUser]", "Sends your avatar or a mentioned users avatar.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isNotEmpty() && event.message.mentions.isEmpty()) {
            event.message.channel.sendMessage(
                error(
                    client,
                    event,
                    "'${args.joinToString(" ")}}' is not a mentioned autoDeleteUser.",
                    commandInfo
                )
            ).await().also { message ->
                onComplete(message, true)
            }
            return
        }

        val user : User
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
        )
            .await().also { onComplete(it, config.auto_delete.bot.content_generation) }
    }
}
