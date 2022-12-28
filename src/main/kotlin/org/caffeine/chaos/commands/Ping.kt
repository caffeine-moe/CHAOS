package org.caffeine.chaos.commands

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.awaitThen
import java.net.URL

class Ping : Command(
    arrayOf("ping", "latency"),
    CommandInfo(
        "Ping",
        "ping [IP/URL]",
        "Checks how long it takes to connect to the discord gateway OR a specified IP or URL in milliseconds."
    )
) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (args.isEmpty()) {
            val pinging = event.message.channel.sendMessage("Pinging...").await()
            val ping = "${pinging.timestamp - event.message.timestamp}"
            pinging.edit(
                MessageBuilder()
                    .appendLine(":ping_pong: Pong!")
                    .appendLine("Latency: ${ping}ms")
            )
                .awaitThen { message -> onComplete(message, true) }
            return
        }
        val pinging = event.message.channel.sendMessage("Pinging...").await()
        val start : Long
        val stop : Long
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
            val selectorManager = ActorSelectorManager(Dispatchers.IO)
            start = System.currentTimeMillis()
            aSocket(selectorManager).tcp().connect(host, 80)
            stop = System.currentTimeMillis()
            selectorManager.close()
        } catch (e : Exception) {
            val err : String = when (e) {
                is UnresolvedAddressException -> {
                    "IP/URL '$url' is invalid."
                }

                is SocketTimeoutException -> {
                    pinging.edit(
                        MessageBuilder()
                            .appendLine(":pensive: Connection timed out")
                            .appendLine("Try a different IP or URL...")
                    )
                        .awaitThen { message -> onComplete(message, true) }
                    return
                }

                else -> ({
                    e.message
                }).toString()
            }
            pinging.edit(error(client, event, err, commandInfo))
            return
        }

        val ping = stop - start
        pinging.edit(
            MessageBuilder()
                .appendLine(":ping_pong: Pong!")
                .appendLine("Target: $url")
                .appendLine("Latency: ${ping}ms")
        ).awaitThen { message -> onComplete(message, true) }
    }
}