package org.caffeine.chaos.commands

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun ping(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}ping") {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("Pinging...")
            .build())
            .thenAccept { message ->
                this.launch {
                    val start = System.currentTimeMillis()
                    val selectorManager = ActorSelectorManager(Dispatchers.IO)
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
    }
    if (event.message.content.lowercase()
            .startsWith("${client.config.prefix}ping ") && event.message.content.lowercase() != "${client.config.prefix}ping"
    ) {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("Pinging...")
            .build())
            .thenAccept { message ->
                this.launch {
                    val url = event.message.content.replaceFirst("${client.config.prefix}ping ", "")
                    val start = System.currentTimeMillis()
                    try {
                        val selectorManager = ActorSelectorManager(Dispatchers.IO)
                        aSocket(selectorManager).tcp().connect(url, 80)
                        selectorManager.close()
                    } catch (e: Exception) {
                        when (e) {
                            is UnresolvedAddressException -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("Error: IP/URL '$url' is invalid.")
                                    .appendLine("Correct usage: `${client.config.prefix}ping IP/URL`")
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
                                    .appendLine("Error: $e")
                                    .build())
                                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                                return@launch
                            }
                        }
                    }
                    val stop = System.currentTimeMillis()
                    val ping = stop - start
                    message.edit(MessageBuilder()
                        .appendLine(":ping_pong: Pong!")
                        .appendLine("Target: $url")
                        .appendLine("Latency: ${ping}ms")
                        .build())
                        .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                }
            }

    }
}





