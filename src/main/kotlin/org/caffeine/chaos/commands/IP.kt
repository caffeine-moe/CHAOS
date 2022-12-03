package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.normalHTTPClient
import java.net.InetAddress
import java.net.URL

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
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            event.channel.sendMessage(error(client, event, "No IP/URL specified.", commandInfo))
                .await().also { message -> onComplete(message, true) }
            return
        }
        event.channel.sendMessage(MessageBuilder().appendLine("Looking up IP/URL"))
            .await().also { message ->
                val url = args.joinToString(" ")
                try {
                    val host = if (url.contains("://")) {
                        withContext(Dispatchers.IO) {
                            URL(url).host
                        }
                    } else {
                        val selectorManager = ActorSelectorManager(Dispatchers.IO)
                        val con = aSocket(selectorManager).tcp().connect(url, 443)
                        con.remoteAddress.toJavaAddress().hostname
                    }
                    val ip = withContext(Dispatchers.IO) {
                        InetAddress.getByName(host).hostAddress
                    }
                    val response =
                        normalHTTPClient.request("http://ip-api.com/json/$ip?fields=28561407")
                    val parsedResponse =
                        json.decodeFromString<IPAPIResponse>(response.bodyAsText())
                    when (parsedResponse.status == "success") {
                        true -> {
                            message.edit(
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
                                .await().also { message -> onComplete(message, true) }
                        }

                        false -> {
                            message.edit(error(client, event, parsedResponse.message, commandInfo))
                                .await().also { message -> onComplete(message, true) }
                        }
                    }
                } catch (e : Exception) {
                    when (e) {
                        is UnresolvedAddressException -> {
                            message.edit(error(client, event, "IP/URL is invalid.", commandInfo))
                                .await().also { message -> onComplete(message, true) }
                            return
                        }

                        is SocketTimeoutException -> {
                            message.edit(
                                MessageBuilder()
                                    .appendLine(":pensive: Connection timed out")
                                    .appendLine("Try a different IP or URL...")
                            )
                                .await().also { message -> onComplete(message, true) }
                            return
                        }

                        else -> {
                            message.edit(error(client, event, e.message.toString(), commandInfo))
                                .await().also { message -> onComplete(message, true) }
                            return
                        }
                    }
                }
            }
    }
}
