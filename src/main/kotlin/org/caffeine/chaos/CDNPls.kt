package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.log

fun cdnpls(client : Client, event : ClientEvents.MessageCreate, args: List<String>) {
    val regex = Regex(pattern = "https?://media\\.discordapp\\.net/attachments/\\d{18,19}/\\d{18,19}/\\S*")
    val url = regex.findAll(event.message.content)
    if (url.toList().isNotEmpty()) {
        val cdnpls = url.first().value.replace("media.discordapp.net", "cdn.discordapp.com")
        log("$cdnpls | ${event.message.author.id} | ${event.message.guild?.name}")
    }
}