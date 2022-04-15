package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

/*
fun Purge(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.message.content.lowercase() == "${config.prefix}purge" || event.message.content.lowercase() == "${config.prefix}sclear") {
            event.channel.sendMessage("Incorrect usage '${event.message.content}'\nError: Not enough parameters!\nCorrect usage: `${config.prefix}purge Int`")
                .thenAccept { message -> }
        }
        if (event.message.content.lowercase().startsWith("${config.prefix}purge ") || event.message.content.lowercase()
                .startsWith("${config.prefix}sclear ") && event.message.content.lowercase() != "${config.prefix}purge" && event.message.content.lowercase() != "${config.prefix}sclear"
        ) {
            val number = event.message.content.lowercase().replace("[^0-9]".toRegex(), "")
            try {
                val number = event.message.content.lowercase().replace("[^0-9]".toRegex(), "").toInt()
                if (number <= 0) {
                    event.channel.sendMessage("Incorrect usage '${event.message.content}'\nError: Int must be higher than 0!\nCorrect usage: `${config.prefix}purge Int`")
                        .thenAccept { message -> }
                    return@thread
                }
                var done = 0
                val count = event.channel.messagesAsStream.filter { x -> x.author == event.messageAuthor }.count()
                if (count <= 0) {
                    event.channel.sendMessage(
                        "There is nothing to delete!"
                    ).thenAccept { message -> }
                    return@thread
                }
                for (Message: Message in event.channel.messagesAsStream.filter { x -> x.author == event.messageAuthor }) {
                    if (done % 8 == 0 && done != 0) {
                        Thread.sleep(4500)
                    }
                    Message.delete()
                    Thread.sleep(500)
                    done++
                    if (done == number) break
                }
                if (done > 1) {
                    event.channel.sendMessage("Removed $done messages!").thenAccept { message -> }
                }
                if (done == 1) {
                    event.channel.sendMessage("Removed $done message!").thenAccept { message -> }
                }
            } catch (e: Exception) {
                when (e) {
                    is NumberFormatException -> {
                        event.channel.sendMessage("Incorrect usage '${event.message.content}'\nError: '$number' is not an integer!\nCorrect usage: `${config.prefix}purge Int`")
                            .thenAccept { message -> }
                    }
                    else -> {
                        println(e)
                    }
                }
            }
        }
    }
}*/
