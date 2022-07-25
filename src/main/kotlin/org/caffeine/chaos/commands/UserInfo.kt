package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.models.interfaces.DiscordUser
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.config


class UserInfo :
    Command(
        arrayOf("userinfo", "info"),
        CommandInfo("UserInfo", "info <@user>", "Displays information about a mentioned user.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) : Unit =
        coroutineScope {
            var error = ""
            var usr : DiscordUser = client.user
            if (event.message.mentions.isNotEmpty()) {
                usr = event.message.mentions.values.first()
            } else if (event.message.mentions.isEmpty() && args.isNotEmpty()) {
                error = "'${args.joinToString(" ")}' is not a mentioned user."
            }
            if (error.isNotBlank()) {
                event.message.channel.sendMessage(error(client, event, error, commandInfo))
                    .thenAccept { message ->
                        this.launch { onComplete(message, client, true) }
                    }
                return@coroutineScope
            }
            val usrInfo = usr
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = java.util.Date(client.utils.convertIdToUnix(usrInfo.id))
            val acd = sdf.format(date)
            event.message.channel.sendMessage(
                MessageBuilder()
                    .appendLine("**User info for ${usr.discriminatedName}**")
                    .appendLine("**Id:** ${usrInfo.id}")
                    .appendLine("**Username:** ${usrInfo.username}")
                    .appendLine("**Discriminator:** ${usrInfo.discriminator}")
                    .appendLine("**Avatar:** <${usrInfo.avatarUrl()}>")
                    .appendLine("**Avatar Decoration:** ${usrInfo.avatarDecoration}")
                    .appendLine("**Banner:** <${usrInfo.banner}>")
                    .appendLine("**Banner Colour:** ${usrInfo.bannerColor}")
                    .appendLine("**Account Creation Date:** $acd")
                    .build()
            ).thenAccept { message ->
                this.launch { onComplete(message, client, config.auto_delete.bot.content_generation) }
            }
            return@coroutineScope
        }
}