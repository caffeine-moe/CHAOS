package org.caffeine.chaos.commands

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread


@Serializable
data class ipApiResponse(
    val status: String,
    val continent: String,
    val country: String,
    val regionName: String,
    val city: String,
    val zip: String,
    val timezone: String,
    val isp: String,
    val proxy: Boolean,
    val query: String,
)

/*
fun IP(client: DiscordApi, event: MessageCreateEvent, config: Config) {
    thread {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
        if (event.messageContent.lowercase() == "${config.prefix}ip") {
            event.channel.sendMessage(
                "Incorrect usage '${event.messageContent}'\nError: No URL specified\nCorrect usage: `${config.prefix}ip IP/URL`"
            ).thenAccept { message -> }
        }
        if (event.messageContent.lowercase()
                .startsWith("${config.prefix}ip ") && event.messageContent.lowercase() != "${config.prefix}ip"
        ) {
            event.channel.sendMessage("Looking up IP...")
                .thenAccept { message ->
                    val url = event.messageContent.replaceFirst("${config.prefix}ip ", "")
                    try {
                        val ip: InetAddress = InetAddress.getByName(url)
                        val cleanip = ip.hostAddress
                        val rqurl = "http://ip-api.com/json/${cleanip}?fields=1237817"
                        val response = URL(rqurl).readText()
                        val parsedresponse = Json.decodeFromString<Response>(response)
                        when (parsedresponse.status) {
                            "success" -> {
                                message.edit("**Information for IP/URL $url**\nContinent: ${parsedresponse.continent}\nCountry: ${parsedresponse.country}\nRegion: ${parsedresponse.regionName}\nCity: ${parsedresponse.city}\nZip/Postal: ${parsedresponse.zip}\nTimezone: ${parsedresponse.timezone}\nISP: ${parsedresponse.isp}\nProxy: ${parsedresponse.proxy}")
                                    .thenAccept { message -> }
                            }
                            "fail" -> {
                                message.edit(
                                    "Incorrect usage '${event.messageContent}'\nError: Unable to lookup IP/URL '$url'\nCorrect usage: `${config.prefix}ip IP/URL`"
                                ).thenAccept { message -> }
                            }
                        }
                    } catch (e: Exception) {
                        if (e == SocketTimeoutException()) {
                            message.edit(
                                ":pensive: Connection timed out\nTry a different IP or URL..."
                            ).thenAccept { message -> }
                        }
                        message.edit(
                            "Incorrect usage '${event.messageContent}'\nError: ${e.message}\nCorrect usage: `${config.prefix}ip IP/URL`"
                        ).thenAccept { message -> }
                    }
                }
        }
    }
}*/
