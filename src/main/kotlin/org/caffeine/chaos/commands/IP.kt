package org.caffeine.chaos.commands

import io.ktor.network.sockets.*
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents


class IP : Command(arrayOf("ip"), CommandInfo("IP", "ip <IP/URL>", "Looks up information about a specified IP/URL.")) {
    @Serializable
    private data class IpApiResponse(
        val borders : String = "",
        val callingCode : String = "",
        val capital : String = "",
        val city : String = "",
        val connection : Connection = Connection(),
        val continent : String = "",
        val continentCode : String = "",
        val country : String = "",
        val countryCode : String = "",
        val currency : Currency = Currency(),
        val flag : Flag = Flag(),
        val ip : String = "",
        val isEu : Boolean = false,
        val latitude : Double = 0.0,
        val longitude : Double = 0.0,
        val postal : String = "",
        val region : String = "",
        val regionCode : String = "",
        val security : Security = Security(),
        val success : Boolean = false,
        val message : String = "",
        val timezone : Timezone = Timezone(),
        val type : String = "",
    )

    @Serializable
    private data class Connection(
        val asn : Int = 0,
        val domain : String = "",
        val isp : String = "",
        val org : String = "",
    )

    @Serializable
    private data class Currency(
        val code : String = "",
        val exchangeRate : Int = 0,
        val name : String = "",
        val plural : String = "",
        val symbol : String = "",
    )

    @Serializable
    private data class Flag(
        val emoji : String = "",
        val emojiUnicode : String = "",
        val img : String = "",
    )

    @Serializable
    private data class Security(
        val anonymous : Boolean = false,
        val hosting : Boolean = false,
        val proxy : Boolean = false,
        val tor : Boolean = false,
        val vpn : Boolean = false,
    )

    @Serializable
    private data class Timezone(
        val abbr : String = "",
        val currentTime : String = "",
        val id : String = "",
        val isDst : Boolean = false,
        val offset : Int = 0,
        val utc : String = "",
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            if (args.isEmpty()) {
                event.channel.sendMessage(error(client, event, "No IP/URL specified.", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            event.channel.sendMessage(MessageBuilder().appendLine("Looking up IP/URL").build())
                .thenAccept { message ->
                    this.launch {
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
                                normalHTTPClient.request("https://ipwhois.pro/$ip?key=Sxd2AkU2ZL0YtkSR&security=1&lang=en") {
                                    headers {
                                        append("Referer", "https://ipwhois.io/")
                                    }
                                }
                            val parsedResponse =
                                json.decodeFromString<IpApiResponse>(response.bodyAsText())
                            when (parsedResponse.success) {
                                true -> {
                                    message.edit(MessageBuilder()
                                        .appendLine("**Information for IP/URL $url**")
                                        .appendLine("**IP:** ${parsedResponse.ip}")
                                        .appendLine("**Continent:** ${parsedResponse.continent}")
                                        .appendLine("**Country:** ${parsedResponse.country}")
                                        .appendLine("**Region:** ${parsedResponse.region}")
                                        .appendLine("**City:** ${parsedResponse.city}")
                                        .appendLine("**Zip/Postal:** ${parsedResponse.postal}")
                                        .appendLine("**Timezone:** ${parsedResponse.timezone.id}")
                                        .appendLine("**ISP:** ${parsedResponse.connection.isp}")
                                        .appendLine("**Proxy:** ${parsedResponse.security.proxy}")
                                        .appendLine("**VPN:** ${parsedResponse.security.vpn}")
                                        .appendLine("**Hosting:** ${parsedResponse.security.hosting}")
                                        .appendLine("**Tor:** ${parsedResponse.security.tor}")
                                        .build())
                                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                }
                                false -> {
                                    message.edit(error(client, event, parsedResponse.message, commandInfo))
                                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                }
                            }
                        } catch (e : Exception) {
                            when (e) {
                                is UnresolvedAddressException -> {
                                    message.edit(error(client, event, "IP/URL is invalid.", commandInfo))
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
                                    message.edit(error(client, event, e.message.toString(), commandInfo))
                                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                    return@launch
                                }
                            }
                        }
                    }
                }*/
        }
}