package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/*suspend fun Avatar(client: Client, event: MessageCreateEvent, config: Config) {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.message.content.lowercase() == "${config.prefix}avatar" || event.message.content.lowercase() == "${config.prefix}av") {
            MessageBuilder()
                .append("${event.message.author.discriminatedName}'s Avatar")
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
                    .append("Incorrect usage '${event.message.content}'")
                    .append("Error: '${event.message.content.split(" ")[1]}' is not a mentioned user")
                    .append("Correct usage: `${config.prefix}avatar @user`")
                    .send(event.message.channel, config)
            }
        }
}*/
