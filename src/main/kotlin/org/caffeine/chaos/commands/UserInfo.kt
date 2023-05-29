package org.caffeine.chaos.commands

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.entities.guild.GuildMember
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.awaitThen

class UserInfo :
    Command(
        arrayOf("userinfo", "info"),
        CommandInfo("UserInfo", "info <@user>", "Displays information about a mentioned user.")
    ) {
    override suspend fun onCalled(
        client: Client,
        event: ClientEvent.MessageCreate,
        args: MutableList<String>,
        cmd: String,
    ) {
        var error = ""
        var usr: User = client.user
        if (event.message.mentions.isNotEmpty()) {
            usr = event.message.mentions.values.first()
        } else if (event.message.mentions.isEmpty() && args.isNotEmpty()) {
            error = "'${args.joinToString(" ")}' is not a mentioned user."
        }
        if (error.isNotBlank()) {
            event.message.channel.sendMessage(error(client, event, error, commandInfo))
                .awaitThen { message ->
                    onComplete(message, false)
                }
            return
        }

        val usrInfo = usr
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val acd = sdf.format(usrInfo.id.timestamp.toEpochMilliseconds())

        val message = MessageBuilder()
            .appendLine("**User info for ${usr.discriminatedName}**")
            .appendLine("**Id:** ${usrInfo.id}")
            .appendLine("**Username:** ${usrInfo.username}")
            .appendLine("**Discriminator:** ${usrInfo.discriminator}")
            .appendLine("**Avatar:** <${usrInfo.avatarUrl()}>")
            .appendLine("**Account Creation Date:** $acd")

        if (usr is GuildMember) {
            val rolesList = usr.roles.map { it.value.name }.joinToString { ", " }
            message.appendLine("**Roles:** $rolesList")
        }

        event.message.channel.sendMessage(
            message
        ).awaitThen { message ->
            onComplete(message, true)
        }

        return
    }
}