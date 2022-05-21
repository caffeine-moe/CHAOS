package org.caffeine.chaos

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.scamLinks
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class AntiScamResponse(
    val domains: List<String>,
)

suspend fun antiScam(client: Client, event: MessageCreateEvent) {
    if (event.message.author.id != client.user.id) {
        val start = System.currentTimeMillis()
        var scamLink = ""
        val scam = scamLinks.any { link -> scamLink = link; event.message.content.contains(link) }
        if (scam) {
            if (client.config.logger.anti_scam) {
                log("Found scam link \"${scamLink}\" in channel ${event.channel.id} by ${event.message.author.discriminatedName} in ${(System.currentTimeMillis() - start).absoluteValue}ms.",
                    "ANTI SCAM:")
                if (client.config.anti_scam.block && !event.message.author.isBlocked(client)) {
                    client.user.block(event.message.author.id)
                    log("Blocked user ${event.message.author.discriminatedName} in ${(System.currentTimeMillis() - start).absoluteValue}ms.",
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