package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.asSnowflake
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.awaitThen

class Avatar : Command(
    arrayOf("avatar", "pfp", "av"),
    CommandInfo("Avatar", "av [@User/ID]", "Sends your avatar or a mentioned users avatar.")
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (args.isNotEmpty() && event.message.mentions.isEmpty() && !Snowflake.validValues.contains(
                args.first().toULong()
            )
        ) {
            event.message.channel.sendMessage(
                error(
                    client,
                    event,
                    "'${args.joinToString(" ")}}' is not a mention or valid ID.",
                    commandInfo
                )
            ).awaitThen {
                onComplete(it, false)
            }
            return
        }

        val user = if (event.message.mentions.isEmpty()) if (args.isNotEmpty()) client.user.fetchUser(
            args.first().asSnowflake()
        ) else client.user
        else event.message.mentions.values.first()

        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("`${user.username}`'s Avatar")
                .addAttachmentFromURL(user.avatarUrl())
        ).awaitThen { onComplete(it, true) }
    }
}