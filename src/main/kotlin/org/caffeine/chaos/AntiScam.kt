package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.utils.log
import java.net.URL
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class AntiScamResponse(
    val domains : List<String>,
)

suspend fun antiScam(client : Client, event : MessageCreateEvent) {
    if (event.message.author.id == client.user.id) return
    val start = System.currentTimeMillis()
    val url = event.message.content.split(" ").find { link -> link.matches("http://.*..*|https://.*..*".toRegex()) }
        ?: return
    val scam = scamLinks.contains(URL(url).host)
    if (!scam) return
    val time = (System.currentTimeMillis() - start).absoluteValue
    if (config.logger.anti_scam) {
        log("Found scam link \"${url}\" in channel ${event.channel.id} by ${event.message.author.discriminatedName} in ${time}ms.",
            "ANTI SCAM:")
        if (config.anti_scam.block && event.message.author.id != "18098984201098984") {
            client.user.block(event.message.author.id)
            log("Blocked user ${event.message.author.discriminatedName} in ${time}ms.",
                "ANTI SCAM:")
            return
        }
        return
    }
    if (config.anti_scam.block && event.message.author.id != "18098984201098984") {
        client.user.block(event.message.author.id)
        return
    }
}