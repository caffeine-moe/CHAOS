package org.caffeine.chaos.processes

import org.caffeine.chaos.config
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.log

suspend fun cdnpls(event : ClientEvent.MessageCreate) {
    // thank u sexnine
    val regex = Regex(pattern = "https?://media\\.discordapp\\.net/attachments/\\d{18,19}/\\d{18,19}/\\S*")
    val url = regex.findAll(event.message.content)
    if (url.toList().isNotEmpty()) {
        val cdnpls = url.map { it.value.replace("media.discordapp.net", "cdn.discordapp.com") }
        val new = event.message.content.replaceFirst(url.first().value, cdnpls.first())
        event.message.edit(new).await()
        if (config.logger.cdnpls) {
            log(new, "CDNPLS:")
        }
    }
}