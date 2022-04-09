package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.javacord.api.DiscordApi
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

fun Coin(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
    thread {
        if (event.messageContent.lowercase() == ("${config.prefix}coin")) {
            val headortail = arrayOf("heads", "tails").random()
            if (headortail == "heads") {
                event.channel.sendMessage(
                    ":coin: Heads!"
                ).thenAccept { message ->  }
                return@thread
            }
            if (headortail == "tails") {
                event.channel.sendMessage(
                    ":coin: Tails!"
                ).thenAccept { message -> }
                return@thread
            }
        }
    }
}