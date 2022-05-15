package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun userInfo(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}info" && event.message.mentions.isNullOrEmpty()) {
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
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}info ") && event.message.content.lowercase() != ("${client.config.prefix}info") && event.message.mentions?.isNotEmpty() == true
    ) {
        val usr = event.message.mentions!!.first()
        val usrinfo = event.message.mentions!!.first().userInfo()
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
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}info ") && event.message.content.lowercase() != ("${client.config.prefix}info") && event.message.mentions?.isEmpty() == true
    ) {
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("Incorrect usage '${event.message.content}'")
                .appendLine("Error: '${
                    event.message.content.split(" ").drop(1).joinToString(" ")
                }' is not a mentioned user.")
                .appendLine("Correct usage: `${client.config.prefix}info @user`")
                .build()).thenAccept { message ->
            this.launch { onComplete(message, client, true) }
            return@thenAccept
        }
    }
}