package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent


class UserInfo : Command(arrayOf("info", "userinfo")) {
    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            if (args.isEmpty() && event.message.mentions.isEmpty()) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: No users were mentioned.")
                        .appendLine("Correct usage: `${client.config.prefix}info @user`")
                        .build()).thenAccept { message ->
                    this.launch { onComplete(message, client, true) }
                }
                return@coroutineScope
            }
            if (event.message.mentions.isNotEmpty()) {
                val usr = event.message.mentions.first()
                val usrinfo = event.message.mentions.first().userInfo()
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = java.util.Date(client.convertIdToUnix(usrinfo.id))
                val acd = sdf.format(date)
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("**User info for ${usr.discriminatedName}**")
                        .appendLine("**Id:** ${usrinfo.id}")
                        .appendLine("**Username:** ${usrinfo.username}")
                        .appendLine("**Discriminator:** ${usrinfo.discriminator}")
                        .appendLine("**Avatar:** <${usrinfo.avatar}>")
                        .appendLine("**Avatar Decoration:** ${usrinfo.avatarDecoration}")
                        .appendLine("**Banner:** <${usrinfo.banner}>")
                        .appendLine("**Banner Colour:** ${usrinfo.bannerColor}")
                        .appendLine("**Accent Colour:** ${usrinfo.accentColor}")
                        .appendLine("**Bot:** ${usrinfo.bot}")
                        .appendLine("**Public Flags:** ${usrinfo.publicFlags}")
                        .appendLine("**Account Creation Date:** $acd")
                        .build()).thenAccept { message ->
                    this.launch { onComplete(message, client, true) }
                    return@thenAccept
                }
            }
            if (event.message.mentions.isEmpty() && args.isNotEmpty()) {
                event.channel.sendMessage(
                    MessageBuilder()
                        .appendLine("Incorrect usage '${event.message.content}'")
                        .appendLine("Error: '${args.joinToString(" ")}' is not a mentioned user.")
                        .appendLine("Correct usage: `${client.config.prefix}info @user`")
                        .build()).thenAccept { message ->
                    this.launch { onComplete(message, client, true) }
                    return@thenAccept
                }
            }
        }
}