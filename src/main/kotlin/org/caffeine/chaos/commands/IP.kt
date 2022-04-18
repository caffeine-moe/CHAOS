package org.caffeine.chaos.commands

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.httpclient
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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

suspend fun IP(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss"))
    if (event.message.content.lowercase() == "${config.prefix}ip") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** No IP/URL specified")
            .appendLine("**Correct usage:** `${config.prefix}ip IP/URL`")
            .build(), config, client)
            .thenAccept { message -> this.launch { bot(message, config) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${config.prefix}ip ") && event.message.content.lowercase() != "${config.prefix}ip"
    ) {
        event.channel.sendMessage(MessageBuilder().appendLine("Looking up IP/URL").build(), config, client)
            .thenAccept { message ->
                this.launch {
                    val url = event.message.content.replaceFirst("${config.prefix}ip ", "")
                    try {
                        val ip: InetAddress = InetAddress.getByName(url)
                        val cleanip = ip.hostAddress
                        val rqurl = "http://ip-api.com/json/${cleanip}?fields=1237817"
                        val response = httpclient.request(rqurl) {
                            method = HttpMethod.Get
                        }
                        val parsedresponse = Json { ignoreUnknownKeys = true }.decodeFromString<ipApiResponse>(response.body())
                        when (parsedresponse.status) {
                            "success" -> {
                                message.edit(MessageBuilder()
                                    .appendLine("**Information for IP/URL $url**")
                                    .appendLine("**Continent:** ${parsedresponse.continent}")
                                    .appendLine("**Country:** ${parsedresponse.country}")
                                    .appendLine("**Region:** ${parsedresponse.regionName}")
                                    .appendLine("**City:** ${parsedresponse.city}")
                                    .appendLine("**City:** ${parsedresponse.city}")
                                    .appendLine("**Zip/Postal:** ${parsedresponse.zip}")
                                    .appendLine("**Zip/Postal:** ${parsedresponse.zip}")
                                    .appendLine("**Timezone:** ${parsedresponse.timezone}")
                                    .appendLine("**ISP:** ${parsedresponse.isp}")
                                    .appendLine("**Proxy:** ${parsedresponse.proxy}")
                                    .build(), config)
                                    .thenAccept { message -> this.launch { bot(message, config) } }
                            }
                            "fail" -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("**Error:** Unable to lookup IP/URL '$url'")
                                    .appendLine("**Correct usage:** `${config.prefix}ip IP/URL`")
                                    .build(), config)
                                    .thenAccept { message -> this.launch { bot(message, config) } }
                            }
                        }
                    } catch (e: Exception) {
                        println(e)
                    }
                }
            }
    }
}
