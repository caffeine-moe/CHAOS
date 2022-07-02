package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Ping : Command(arrayOf("ping", "latency"),
    CommandInfo("Ping",
        "ping [IP/URL]",
        "Checks how long it takes to connect to the discord gateway OR a specified IP or URL in milliseconds.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            if (args.isEmpty()) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Pinging...")
                    .build())
                    .thenAccept { message ->
                        this.launch {
                            val selectorManager = ActorSelectorManager(Dispatchers.IO)
                            val start = System.currentTimeMillis()
                            val serverSocket = aSocket(selectorManager).tcp().connect("gateway.discord.gg", 80)
                            val stop = System.currentTimeMillis()
                            withContext(Dispatchers.IO) {
                                serverSocket.close()
                            }
                            val ping = stop - start
                            message.edit(MessageBuilder()
                                .appendLine(":ping_pong: Pong!")
                                .appendLine("Target: Discord API")
                                .appendLine("Latency: ${ping}ms")
                                .build())
                                .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                        }
                    }
                return@coroutineScope
            }
            event.channel.sendMessage(MessageBuilder()
                .appendLine("Pinging...")
                .build())
                .thenAccept { message ->
                    this.launch {
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
                                    message.edit(MessageBuilder()
                                        .appendLine(":pensive: Connection timed out")
                                        .appendLine("Try a different IP or URL...")
                                        .build())
                                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                    return@launch
                                }
                                else -> {
                                    e.message!!
                                }
                            }
                            message.edit(error(client, event, err, commandInfo))
                            return@launch
                        }
                        val ping = stop - start
                        message.edit(MessageBuilder()
                            .appendLine(":ping_pong: Pong!")
                            .appendLine("Target: $url")
                            .appendLine("Latency: ${ping}ms")
                            .build())
                            .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                    }
                }*/

        }
}