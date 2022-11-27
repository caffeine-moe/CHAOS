package org.caffeine.chaos

import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.api.utils.log

suspend fun cdnpls(event : ClientEvent.MessageCreate) {
    // thank u sexnine
    val regex = Regex(pattern = "https?://media\\.discordapp\\.net/attachments/\\d{18,19}/\\d{18,19}/\\S*")
    val url = regex.findAll(event.message.content)
    if (url.toList().isNotEmpty()) {
        val cdnpls = url.first().value.replace("media.discordapp.net", "cdn.discordapp.com")
        event.message.edit(event.message).await()
        if (config.logger.cdnpls) {
            log(cdnpls, "CDNPLS:")
        }
    }
}
