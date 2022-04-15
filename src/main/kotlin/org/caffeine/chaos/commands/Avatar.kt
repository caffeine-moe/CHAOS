package org.caffeine.chaos.commands

import io.ktor.util.Identity.decode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.client.message.deleteMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.CoroutineContext

suspend fun Avatar(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
    if (event.message.content.lowercase() == "${config.prefix}avatar" || event.message.content.lowercase() == "${config.prefix}av" && client.user.avatarUrl() != "") {
        event.message.channel.sendMessage(MessageBuilder()
            .appendLine("${client.user.discriminatedName}'s avatar")
            .appendLine(client.user.avatarUrl())
            .build(), config, client).thenAccept { message ->
            this.launch { bot(message, config) }
        }
    }
/*    if (event.message.content.lowercase()
            .startsWith("${config.prefix}av") && event.message.content.lowercase() != ("${config.prefix}av") || event.message.content.lowercase()
            .startsWith("${config.prefix}avatar") && event.message.content.lowercase() != ("${config.prefix}avatar")
    ) {
        if (event.message.mentionedUsers.isNotEmpty()) {
            val usr = event.message.mentionedUsers.first()
            event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("${usr.discriminatedName}'s Avatar")
                .appendLine(usr.)
                .build(), config, client)
        } else {
            event.message.channel.sendMessage(
                MessageBuilder()
                    .appendLine("Incorrect usage '${event.message.content}'")
                    .appendLine("Error: '${event.message.content.split(" ")[1]}' is not a mentioned user")
                    .appendLine("Correct usage: `${config.prefix}avatar @user`")
                    .build(), config, client).thenAccept { message ->
                this.launch { bot(message, config) }
            }
        }
    }*/
}