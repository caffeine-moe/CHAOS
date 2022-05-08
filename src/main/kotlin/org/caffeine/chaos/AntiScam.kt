package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.scamlinks
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class AntiScamResponse(
    val domains: List<String>,
)

suspend fun antiScam(client: Client, event: MessageCreateEvent) {
    if (event.message.author.id != client.user.id) {
        val start = System.currentTimeMillis()
        var scamlink = ""
        val scam = scamlinks.any { link -> scamlink = link; event.message.content.contains(link) }
        if (scam) {
            if (client.config.logger.anti_scam) {
                log("Found scam link \"${scamlink}\" in channel ${event.channel.id} by ${event.message.author.discriminatedName} in ${(start - System.currentTimeMillis()).absoluteValue}ms.",
                    "ANTI SCAM:")
                if (client.config.anti_scam.block) {
                    client.user.block(event.message.author.id)
                    log("Blocked user ${event.message.author.discriminatedName} in ${(start - System.currentTimeMillis()).absoluteValue}ms.",
                        "ANTI SCAM:")
                    return
                }
                return
            }
            if (client.config.anti_scam.block) {
                client.user.block(event.message.author.id)
                return
            }
        }
    }
}