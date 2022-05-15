package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.Command
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient
import java.nio.charset.MalformedInputException

class Haste : Command(arrayOf("haste")) {

    @kotlinx.serialization.Serializable
    private data class HasteResponse(
        val key: String,
    )

    override suspend fun onCalled(client: Client, event: MessageCreateEvent, args: MutableList<String>, cmd: String) =
        coroutineScope {
            if (!event.message.attachments.isNullOrEmpty()) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Creating haste...")
                    .build()).thenAccept { message ->
                    this.launch {
                        val response = normalHTTPClient.post("https://www.toptal.com/developers/hastebin/documents") {
                            try {
                                setBody(normalHTTPClient.get(event.message.attachments?.first()?.url.toString())
                                    .bodyAsText())
                            } catch (e: Exception) {
                                if (e is MalformedInputException) {
                                    message.edit(MessageBuilder()
                                        .appendLine("**Incorrect usage** '${event.message.content} [${event.message.attachments?.first()?.filename}]'")
                                        .appendLine("**Error:** Unable to parse text from attached file. You must only upload text documents.")
                                        .appendLine("**Correct usage:** `${client.config.prefix}haste [file.txt]`")
                                        .build())
                                        .thenAccept { this@coroutineScope.launch { onComplete(it, client, true) } }
                                    return@launch
                                }
                            }
                        }
                        val haste = json.decodeFromString<HasteResponse>(response.bodyAsText())
                        message.edit(MessageBuilder()
                            .appendLine("https://www.toptal.com/developers/hastebin/${haste.key}")
                            .build()).thenAccept {
                            this.launch {
                                onComplete(it,
                                    client,
                                    client.config.auto_delete.bot.content_generation)
                            }
                        }
                    }
                }
                return@coroutineScope
            }
            if (args.isNotEmpty() && event.message.attachments?.isEmpty() == true) {
                event.channel.sendMessage(MessageBuilder()
                    .appendLine("Creating haste...")
                    .build()).thenAccept { message ->
                    this.launch {
                        val response = normalHTTPClient.post("https://www.toptal.com/developers/hastebin/documents") {
                            setBody(args.joinToString(" "))
                        }
                        val haste = json.decodeFromString<HasteResponse>(response.bodyAsText())
                        message.edit(MessageBuilder()
                            .appendLine("https://www.toptal.com/developers/hastebin/${haste.key}")
                            .build()).thenAccept { message ->
                            this.launch {
                                onComplete(message,
                                    client,
                                    client.config.auto_delete.bot.content_generation)
                            }
                        }
                    }
                }
            }
        }
}