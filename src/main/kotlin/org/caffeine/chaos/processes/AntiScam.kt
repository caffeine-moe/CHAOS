package org.caffeine.chaos.processes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.json
import org.caffeine.chaos.api.utils.log
import org.caffeine.chaos.api.utils.normalHTTPClient
import org.caffeine.chaos.config
import org.caffeine.chaos.scamLinks
import java.net.URL
import kotlin.math.absoluteValue

@kotlinx.serialization.Serializable
data class AntiScamResponse(
    val domains : List<String>,
)

suspend fun fetchAntiScam() {
    scamLinks =
        json.decodeFromString<AntiScamResponse>(
            normalHTTPClient.get("https://raw.githubusercontent.com/nikolaischunk/discord-phishing-links/main/domain-list.json")
                .bodyAsText()
        ).domains
}

suspend fun antiScam(client : Client, event : ClientEvent.MessageCreate) {
    if (event.message.author.id == client.user.id) return
    val start = System.currentTimeMillis()
    val url = event.message.content.split(" ").find { link -> link.matches("http://.*..*|https://.*..*".toRegex()) }
        ?: return
    val scam = scamLinks.contains(URL(url).host)
    if (!scam) return
    val time = (System.currentTimeMillis() - start).absoluteValue
    if (config.logger.antiScam) {
        log(
            "Found scam link \"${url}\" in channel ${event.message.channel.id} by ${event.message.author.discriminatedName} in ${time}ms.",
            "ANTI SCAM:"
        )
        if (config.antiScam.block && event.message.author.id.toString() != "18098984201098984") {
            client.user.block(event.message.author)
            log(
                "Blocked autoDeleteUser ${event.message.author.discriminatedName} in ${time}ms.",
                "ANTI SCAM:"
            )
            return
        }
        return
    }
    if (config.antiScam.block && event.message.author.id.toString() != "18098984201098984") {
        client.user.block(event.message.author)
        return
    }
}