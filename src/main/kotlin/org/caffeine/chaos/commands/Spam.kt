package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.javacord.api.entity.message.MessageBuilder
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

private var cock = false

fun Spam(event: MessageCreateEvent, config: Config) {
    thread {
        cock = false
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.messageContent.lowercase() == "${config.prefix}spam") {
            MessageBuilder()
                .append("**Incorrect usage:** '${event.messageContent}'")
                .append("**Error:** Not enough parameters!")
                .append("Correct usage: `${config.prefix}spam String Int`")
                .send(event.channel)
                .thenAccept { message ->  }
        }
        if (event.messageContent.lowercase()
                .startsWith("${config.prefix}spam ") && event.messageContent.lowercase() != "${config.prefix}spam "
        ) {
            val msg = event.messageContent.removePrefix("${config.prefix}spam ").split(" ")
            try {
                val number = msg[msg.lastIndex].toInt()
                val stringbuilder = StringBuilder()
                for (str: String in msg.dropLast(1)) {
                    stringbuilder.append("$str ")
                }
                val string = stringbuilder.toString().trim()
                if (number <= 0) {
                    MessageBuilder()
                        .append("**Incorrect usage:** '${event.messageContent}'")
                        .append("**Error:** Int must be higher than 0!")
                        .append("**Correct usage:** `${config.prefix}spam String Int`")
                        .send(event.channel)
                        .thenAccept { message ->  }
                    return@thread
                }
                var done = 0
                while (done < number) {
                    if (cock) {
                        break
                    }
                    if (done % 8 == 0 && done != 0) {
                        Thread.sleep(4500)
                    }
                    event.channel.sendMessage(string)
                    Thread.sleep(500)
                    done++
                }
                if (done > 1) {
                    event.channel.sendMessage("Done spamming '$string' $done times!")
                        .thenAccept { message ->  }
                }
                if (done == 1) {
                    event.channel.sendMessage("Done spamming '$string' once!")
                        .thenAccept { message ->  }
                }
            } catch (e: Exception) {
                when (e) {
                    is java.lang.NumberFormatException -> {
                        event.channel.sendMessage("Incorrect usage '${event.messageContent}'\nError: '${msg[msg.last().lastIndex]}' is not an integer!\nCorrect usage: `${config.prefix}spam String Int`")
                            .thenAccept { message ->  }
                    }
                    is IndexOutOfBoundsException -> {
                        event.channel.sendMessage("Incorrect usage '${event.messageContent}'\nError: Not enough parameters!\nCorrect usage: `${config.prefix}spam String Int`")
                            .thenAccept { message ->  }
                    }
                    else -> {
                        println(e)
                    }
                }
            }
        }
    }
}

fun SSpam(event: MessageCreateEvent, config: Config) {
    thread {
        if (event.messageContent.lowercase() == "${config.prefix}sspam") {
            cock = true
        }
    }
}