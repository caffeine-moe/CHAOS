package org.caffeine.chaos.commands

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.httpclient
import java.net.InetAddress

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

suspend fun ip(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}ip") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** No IP/URL specified")
            .appendLine("**Correct usage:** `${client.config.prefix}ip IP/URL`")
            .build(), client)
            .thenAccept { message -> this.launch { bot(message, client) } }
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}ip ") && event.message.content.lowercase() != "${client.config.prefix}ip"
    ) {
        event.channel.sendMessage(MessageBuilder().appendLine("Looking up IP/URL").build(), client)
            .thenAccept { message ->
                this.launch {
                    val url = event.message.content.replaceFirst("${client.config.prefix}ip ", "")
                    try {
                        val ip: InetAddress = withContext(Dispatchers.IO) {
                            InetAddress.getByName(url)
                        }
                        val cleanip = ip.hostAddress
                        val rqurl = "http://ip-api.com/json/${cleanip}?fields=1237817"
                        val response = httpclient.request(rqurl) {
                            method = HttpMethod.Get
                        }
                        val parsedresponse =
                            Json { ignoreUnknownKeys = true }.decodeFromString<ipApiResponse>(response.body())
                        when (parsedresponse.status) {
                            "success" -> {
                                message.edit(MessageBuilder()
                                    .appendLine("**Information for IP/URL $url**")
                                    .appendLine("**IP:** ${parsedresponse.query}")
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
                                    .build(), client)
                                    .thenAccept { message -> this.launch { bot(message, client) } }
                            }
                            "fail" -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("**Error:** Unable to lookup IP/URL '$url'")
                                    .appendLine("**Correct usage:** `${client.config.prefix}ip IP/URL`")
                                    .build(), client)
                                    .thenAccept { message -> this.launch { bot(message, client) } }
                            }
                        }
                    } catch (e: Exception) {
                        println(e)
                    }
                }
            }
    }
}
