package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.BASE_URL
import org.caffeine.chaos.api.httpclient
import org.javacord.api.DiscordApi
import org.javacord.api.entity.channel.GroupChannel
import org.javacord.api.event.message.MessageCreateEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

private var chid = mutableListOf<String>()

fun LGDM(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.messageContent.lowercase() == ("${config.prefix}lgdm") || event.messageContent.lowercase() == ("${config.prefix}leavegroups")) {
            for (dm: GroupChannel in client.groupChannels) {
                chid.add(dm.id.toString())
            }
            runBlocking {
                try {
                    for (item: String in chid) {
                        httpclient.request<String>("$BASE_URL/channels/$item") {
                            method = HttpMethod.Delete
                            headers {
                                append(HttpHeaders.Authorization, config.token)
                            }
                        }
                        Thread.sleep(500)
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                }
            }
        }
    }
}
