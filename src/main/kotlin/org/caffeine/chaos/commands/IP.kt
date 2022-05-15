package org.caffeine.chaos.commands

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient
import java.net.InetAddress

@Serializable
data class IpApiResponse(
    val borders: String = "",
    val callingCode: String = "",
    val capital: String = "",
    val city: String = "",
    val connection: Connection = Connection(),
    val continent: String = "",
    val continentCode: String = "",
    val country: String = "",
    val countryCode: String = "",
    val currency: Currency = Currency(),
    val flag: Flag = Flag(),
    val ip: String = "",
    val isEu: Boolean = false,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val postal: String = "",
    val region: String = "",
    val regionCode: String = "",
    val security: Security = Security(),
    val success: Boolean = false,
    val message: String = "",
    val timezone: Timezone = Timezone(),
    val type: String = "",
)

@Serializable
data class Connection(
    val asn: Int = 0,
    val domain: String = "",
    val isp: String = "",
    val org: String = "",
)

@Serializable
data class Currency(
    val code: String = "",
    val exchangeRate: Int = 0,
    val name: String = "",
    val plural: String = "",
    val symbol: String = "",
)

@Serializable
data class Flag(
    val emoji: String = "",
    val emojiUnicode: String = "",
    val img: String = "",
)

@Serializable
data class Security(
    val anonymous: Boolean = false,
    val hosting: Boolean = false,
    val proxy: Boolean = false,
    val tor: Boolean = false,
    val vpn: Boolean = false,
)

@Serializable
data class Timezone(
    val abbr: String = "",
    val currentTime: String = "",
    val id: String = "",
    val isDst: Boolean = false,
    val offset: Int = 0,
    val utc: String = "",
)

suspend fun ip(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}ip") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** No IP/URL specified")
            .appendLine("**Correct usage:** `${client.config.prefix}ip IP/URL`")
            .build())
            .thenAccept { message -> this.launch { onComplete(message, client, true) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}ip ") && event.message.content.lowercase() != "${client.config.prefix}ip"
    ) {
        event.channel.sendMessage(MessageBuilder().appendLine("Looking up IP/URL").build())
            .thenAccept { message ->
                this.launch {
                    val url = event.message.content.replaceFirst("${client.config.prefix}ip ", "")
                    try {
                        val selectorManager = ActorSelectorManager(Dispatchers.IO)
                        val con = aSocket(selectorManager).tcp().connect(url, 443)
                        val realhost = con.remoteAddress.toJavaAddress().hostname
                        val ip = withContext(Dispatchers.IO) {
                            InetAddress.getByName(realhost).hostAddress
                        }
                        val response =
                            normalHTTPClient.request("https://ipwhois.pro/$ip?key=Sxd2AkU2ZL0YtkSR&security=1&lang=en") {
                                headers {
                                    append("Referer", "https://ipwhois.io/")
                                }
                            }
                        val parsedresponse =
                            json.decodeFromString<IpApiResponse>(response.bodyAsText())
                        when (parsedresponse.success) {
                            true -> {
                                message.edit(MessageBuilder()
                                    .appendLine("**Information for IP/URL $url**")
                                    .appendLine("**IP:** ${parsedresponse.ip}")
                                    .appendLine("**Continent:** ${parsedresponse.continent}")
                                    .appendLine("**Country:** ${parsedresponse.country}")
                                    .appendLine("**Region:** ${parsedresponse.region}")
                                    .appendLine("**City:** ${parsedresponse.city}")
                                    .appendLine("**Zip/Postal:** ${parsedresponse.postal}")
                                    .appendLine("**Timezone:** ${parsedresponse.timezone.id}")
                                    .appendLine("**ISP:** ${parsedresponse.connection.isp}")
                                    .appendLine("**Proxy:** ${parsedresponse.security.proxy}")
                                    .build())
                                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                            }
                            false -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("**Error:** ${parsedresponse.message}")
                                    .appendLine("**Correct usage:** `${client.config.prefix}ip IP/URL`")
                                    .build())
                                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                            }
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is UnresolvedAddressException -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("Error: IP/URL '$url' is invalid.")
                                    .appendLine("Correct usage: `${client.config.prefix}ip IP/URL`")
                                    .build())
                                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                return@launch
                            }
                            is SocketTimeoutException -> {
                                message.edit(MessageBuilder()
                                    .appendLine(":pensive: Connection timed out")
                                    .appendLine("Try a different IP or URL...")
                                    .build())
                                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                return@launch
                            }
                            else -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("Error: ${e.message}")
                                    .appendLine("Correct usage: `${client.config.prefix}ip IP/URL`")
                                    .build())
                                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                return@launch
                            }
                        }
                    }
                }
            }
    }
}
