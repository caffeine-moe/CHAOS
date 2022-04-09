package org.caffeine.chaos.commands

import org.caffeine.chaos.Config
import org.caffeine.chaos.api.Client
import org.caffeine.chaos.api.editMessage
import org.caffeine.chaos.api.messageCreate
import org.caffeine.chaos.api.sendMessage
import org.javacord.api.DiscordApi
import org.javacord.api.entity.user.UserStatus
import org.javacord.api.event.message.MessageCreateEvent
import java.net.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread


suspend fun Ping(client: Client, event: messageCreate, config: Config) {
        if (event.d.content.lowercase() == "${config.prefix}ping") {
            val message = sendMessage(event.d.channel_id,"Pinging...", config)
            val start = System.currentTimeMillis()
            val stop = System.currentTimeMillis()
            val ping = stop - start
        }
            if (event.d.content.lowercase()
                    .startsWith("${config.prefix}ping ") && event.d.content.lowercase() != "${config.prefix}ping"
            ) {
/*                event.channel.sendMessage("Pinging...")
                    .thenAccept { message ->
                        val url = event.messageContent.replaceFirst("${config.prefix}ping ", "")
                        val s = Socket()
                        val a: SocketAddress = InetSocketAddress(url, 80)
                        val timeoutMillis = 2000
                        val start = System.currentTimeMillis()
                        try {
                            s.connect(a, timeoutMillis)
                        } catch (e: io.ktor) {
                            s.close()
                            message.edit("Incorrect usage '${event.messageContent}'\nError: URL '$url' is invalid!\nCorrect usage: `${config.prefix}ping URL`")
                                .thenAccept { message -> bot(message, config) }
                            return@thenAccept
                        } catch (e: Exception) {
                            s.close()
                            if (e == SocketTimeoutException()) {
                                message.edit(
                                    ":pensive: Connection timed out\nTry a different IP or URL..."
                                ).thenAccept { message -> bot(message, config) }
                            }
                            message.edit(
                                e.toString()
                            ).thenAccept { message -> bot(message, config) }
                            return@thenAccept
                        }
                        val stop = System.currentTimeMillis()
                        val ping = stop - start
                        s.close()
                        message.edit(
                            ":ping_pong: Pong!\nTarget: $url\nLatency: ${ping}ms"
                        ).thenAccept { message -> bot(message, config) }
                    }*/
            }
        }




