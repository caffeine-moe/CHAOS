package org.caffeine.chaos.commands

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.Dispatchers
import org.caffeine.octane.client.Client
import org.caffeine.octane.client.ClientEvent
import org.caffeine.octane.utils.MessageBuilder
import org.caffeine.octane.utils.awaitThen
import java.net.InetAddress
import java.net.UnknownHostException

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
        args : List<String>,
        cmd : String,
    ) {
        event.message.channel.sendMessage("Pinging...").awaitThen { pinging ->
            if (args.isEmpty()) {
                val ping = "${System.currentTimeMillis() - pinging.timestamp}"
                pinging.edit(
                    MessageBuilder()
                        .appendLine(":ping_pong: Pong!")
                        .appendLine("Latency: ${ping}ms")
                )
                    .awaitThen { message -> onComplete(message, true) }
                return
            }
            val start : Long
            val stop : Long
            val url = args.joinToString(" ")
            try {
                val host = InetAddress.getByName(url).hostName
                val selectorManager = ActorSelectorManager(Dispatchers.IO)
                start = System.currentTimeMillis()
                aSocket(selectorManager).tcp().connect(host, 80)
                stop = System.currentTimeMillis()
                selectorManager.close()
            } catch (e : Exception) {
                val err : String = when (e) {
                    is UnknownHostException,
                    is UnresolvedAddressException,
                    -> {
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
}