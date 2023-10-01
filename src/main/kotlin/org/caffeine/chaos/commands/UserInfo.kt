package org.caffeine.chaos.commands

import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.entities.Snowflake
import org.caffeine.octane.entities.asSnowflake
import org.caffeine.octane.entities.users.User
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen

class UserInfo :
    Command(
        arrayOf("userinfo", "user"),
        CommandInfo("UserInfo", "user [@User/ID]", "Displays information about a mentioned user, or yourself.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        var error = ""
        var usr : User = client.user
        if (event.message.mentions.isNotEmpty()) {
            usr = event.message.mentions.values.first()
        } else if (event.message.mentions.isEmpty() && args.isNotEmpty()) {
            if (Snowflake.validValues.contains(args.first().toULong()))
                usr = client.user.fetchUser(args.first().asSnowflake())
            else
                error = "'${args.joinToString(" ")}' is not a valid argument."
        }
        if (error.isNotBlank()) {
            event.message.channel.sendMessage(error(client, event, error, commandInfo))
                .awaitThen { message ->
                    onComplete(message, false)
                }
            return
        }
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val acd = sdf.format(usr.id.timestamp.toEpochMilliseconds())
        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("**User info for ${usr.username}**")
                .appendLine("**Id:** ${usr.id}")
                .appendLine("**Username:** ${usr.username}")
                .appendLine("**Avatar:** <${usr.avatarUrl()}>")
                .appendLine("**Account Creation Date:** $acd")
        ).awaitThen { message ->
            onComplete(message, true)
        }

        return
    }
}