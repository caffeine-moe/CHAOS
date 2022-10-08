package org.caffeine.chaos

import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.log

suspend fun cdnpls(event : ClientEvents.MessageCreate) {
    // thank u sexnine
    val regex = Regex(pattern = "https?://media\\.discordapp\\.net/attachments/\\d{18,19}/\\d{18,19}/\\S*")
    val url = regex.findAll(event.message.content)
    if (url.toList().isNotEmpty()) {
        val cdnpls = url.first().value.replace("media.discordapp.net", "cdn.discordapp.com")
        event.message.edit(MessageBuilder().append(event.message.content.replace(url.first().value, cdnpls)).build())
        if (config.logger.cdnpls) {
            log(cdnpls, "CDNPLS:")
        }
    }
}
