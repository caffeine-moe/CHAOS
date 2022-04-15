package org.caffeine.chaos.commands

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent


suspend fun Ping(client: Client, event: MessageCreateEvent, config: Config) = coroutineScope {
    if (event.message.content.lowercase() == "${config.prefix}ping") {
        event.message.channel.sendMessage(MessageBuilder()
            .appendLine("Pinging...")
            .build(), config, client)
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
                        .build(), config)
                }
            }
    }
    if (event.message.content.lowercase()
            .startsWith("${config.prefix}ping ") && event.message.content.lowercase() != "${config.prefix}ping"
    ) {
        event.message.channel.sendMessage(MessageBuilder()
            .appendLine("Pinging...")
            .build(), config, client)
            .thenAccept { message ->
                this.launch {
                    val url = event.message.content.replaceFirst("${config.prefix}ping ", "")
                    val start = System.currentTimeMillis()
                    try {
                            val selectorManager = ActorSelectorManager(Dispatchers.IO)
                            val serverSocket = aSocket(selectorManager).tcp().connect(url, 80)
                       } catch (e: Exception) {
                        when (e) {
                            is UnresolvedAddressException -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Incorrect usage '${event.message.content}'")
                                    .appendLine("Error: IP/URL '$url' is invalid.")
                                    .appendLine("Correct usage: `${config.prefix}ip IP/URL`")
                                    .build(), config)
                                    .thenAccept { message -> this.launch { bot(message, config) } }
                                return@launch
                            }
                            is SocketTimeoutException -> {
                                message.edit(MessageBuilder()
                                    .appendLine(":pensive: Connection timed out")
                                    .appendLine("Try a different IP or URL...")
                                    .build(), config)
                                    .thenAccept { message -> this.launch { bot(message, config) } }
                                return@launch
                            }
                            else -> {
                                message.edit(MessageBuilder()
                                    .appendLine("Error: $e")
                                    .build(), config)
                                    .thenAccept { message -> this.launch { bot(message, config) } }
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
                        .build(), config)
                        .thenAccept { message -> this.launch { bot(message, config) } }
                }
            }

    }
}





