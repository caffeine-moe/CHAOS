package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.caffeine.chaos.utils.json
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen
import org.caffeine.octane.utils.normalHTTPClient
import java.net.InetAddress
import java.net.UnknownHostException

class IP : Command(arrayOf("ip"), CommandInfo("IP", "ip <IP/URL>", "Looks up information about a specified IP/URL.")) {

    @Serializable
    data class IPAPIResponse(
        @SerialName("as")
        val asn : String = "",
        val city : String = "",
        val continent : String = "",
        val continentCode : String = "",
        val country : String = "",
        val countryCode : String = "",
        val currency : String = "",
        val hosting : Boolean = false,
        val isp : String = "",
        val lat : Double = 0.0,
        val lon : Double = 0.0,
        val mobile : Boolean = false,
        val org : String = "",
        val proxy : Boolean = false,
        val region : String = "",
        val regionName : String = "",
        val status : String = "",
        val timezone : String = "",
        val zip : String = "",
        @Transient
        val message : String = "",
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            event.channel.sendMessage(error(client, event, "No IP/URL specified.", commandInfo))
                .awaitThen { message -> onComplete(message, true) }
            return
        }
        var error = ""
        event.channel.sendMessage("Looking up IP/URL")
            .awaitThen { lookup ->
                val url = args.joinToString(" ")
                try {
                    val ip = InetAddress.getByName(url).hostAddress
                    val response =
                        normalHTTPClient.request("http://ip-api.com/json/$ip?fields=28561407")
                    val parsedResponse =
                        json.decodeFromString<IPAPIResponse>(response.bodyAsText())
                    when (parsedResponse.status == "success") {
                        true -> {
                            lookup.edit(
                                MessageBuilder()
                                    .appendLine("**Information for IP/URL $url**")
                                    .appendLine("**IP:** $ip")
                                    .appendLine("**Continent:** ${parsedResponse.continent}")
                                    .appendLine("**Country:** ${parsedResponse.country}")
                                    .appendLine("**Region:** ${parsedResponse.region}")
                                    .appendLine("**City:** ${parsedResponse.city}")
                                    .appendLine("**Zip/Postal:** ${parsedResponse.zip}")
                                    .appendLine("**Timezone:** ${parsedResponse.timezone}")
                                    .appendLine("**ISP:** ${parsedResponse.isp}")
                                    .appendLine("**Proxy:** ${parsedResponse.proxy}")
                                    .appendLine("**Hosting:** ${parsedResponse.hosting}")
                            )
                                .awaitThen { message -> onComplete(message, true) }
                        }

                        false -> {
                            error = parsedResponse.message
                        }
                    }
                } catch (e : Exception) {
                    when (e) {
                        is UnknownHostException, is UnresolvedAddressException -> {
                            error = "IP/URL is invalid."
                        }

                        is SocketTimeoutException -> {
                            lookup.edit(
                                MessageBuilder()
                                    .appendLine(":pensive: Connection timed out")
                                    .appendLine("Try a different IP or URL...")
                            )
                                .awaitThen { message -> onComplete(message, true) }
                            return
                        }

                        else -> {
                            error = e.message.toString()
                            e.printStackTrace()
                        }
                    }
                }
                if (error.isNotBlank()) {
                    lookup.edit(error(client, event, error, commandInfo))
                        .awaitThen { message -> onComplete(message, true) }
                }
            }
    }
}