package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent


class UserInfo : Command(arrayOf("info", "userinfo")) {
    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit =
        coroutineScope {
            var error = ""
            if (args.isEmpty() && event.message.mentions.isEmpty()) {
                error = "Error: No users were mentioned."
            } else if (event.message.mentions.isEmpty() && args.isNotEmpty()) {
                error = "Error: '${args.joinToString(" ")}' is not a mentioned user."
            }
            if (error.isBlank()) {
                val usr = event.message.mentions.first()
                val usrInfo = event.message.mentions.first().userInfo()
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = java.util.Date(client.convertIdToUnix(usrInfo.id))
                val acd = sdf.format(date)
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("**User info for ${usr.discriminatedName}**")
                        .appendLine("**Id:** ${usrInfo.id}")
                        .appendLine("**Username:** ${usrInfo.username}")
                        .appendLine("**Discriminator:** ${usrInfo.discriminator}")
                        .appendLine("**Avatar:** <${usrInfo.avatar}>")
                        .appendLine("**Avatar Decoration:** ${usrInfo.avatarDecoration}")
                        .appendLine("**Banner:** <${usrInfo.banner}>")
                        .appendLine("**Banner Colour:** ${usrInfo.bannerColor}")
                        .appendLine("**Accent Colour:** ${usrInfo.accentColor}")
                        .appendLine("**Bot:** ${usrInfo.bot}")
                        .appendLine("**Public Flags:** ${usrInfo.publicFlags}")
                        .appendLine("**Account Creation Date:** $acd")
                        .build()).thenAccept { message ->
                    this.launch { onComplete(message, client, client.config.auto_delete.bot.content_generation) }
                }
                return@coroutineScope
            }
            event.channel.sendMessage(
                MessageBuilder()
                    .appendLine("Incorrect usage '${event.message.content}'")
                    .appendLine(error)
                    .appendLine("Correct usage: `${client.config.prefix}info @user`")
                    .build()).thenAccept { message ->
                this.launch { onComplete(message, client, true) }
                return@thenAccept
            }
        }
}