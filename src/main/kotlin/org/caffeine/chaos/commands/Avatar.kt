package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.javacord.api.DiscordApi
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

fun Avatar(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.messageContent.lowercase() == "${config.prefix}avatar" || event.messageContent.lowercase() == "${config.prefix}av") {
            MessageBuilder()
                .append("${event.messageAuthor.discriminatedName}'s Avatar")
                .addAttachment(event.messageAuthor.avatar)
                .send(event.channel)
        }
        if (event.messageContent.lowercase()
                .startsWith("${config.prefix}av") && event.messageContent.lowercase() != ("${config.prefix}av") || event.messageContent.lowercase()
                .startsWith("${config.prefix}avatar") && event.messageContent.lowercase() != ("${config.prefix}avatar")
        ) {
            if (event.message.mentionedUsers.isNotEmpty()) {
                val usr = event.message.mentionedUsers.first()
                MessageBuilder()
                    .append("${usr.discriminatedName}'s Avatar")
                    .addAttachment(usr.avatar)
                    .send(event.channel)
            } else {
                MessageBuilder()
                    .append("Incorrect usage '${event.messageContent}'")
                    .append("Error: '${event.messageContent.split(" ")[1]}' is not a mentioned user")
                    .append("Correct usage: `${config.prefix}avatar @user`")
                    .send(event.channel)
            }
        }
    }
}
