package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient
import java.nio.charset.MalformedInputException

class Haste : Command(arrayOf("haste"),
    CommandInfo("haste <Text> OR <file.txt>", "Uploads text OR a text document to Hastebin.")) {

    @kotlinx.serialization.Serializable
    private data class HasteResponse(
        val key: String,
    )

    override suspend fun onCalled(
        client: Client,
        event: MessageCreateEvent,
        args: MutableList<String>,
        cmd: String,
    ): Unit = coroutineScope {
        event.channel.sendMessage(MessageBuilder().appendLine("Creating haste...").build()).thenAccept { message ->
            launch {
                var body = ""
                if (args.isNotEmpty()) {
                    body = args.joinToString(" ")
                }
                if (event.message.attachments.isNotEmpty()) {
                    try {
                        body = normalHTTPClient.get(event.message.attachments.first().url).bodyAsText()
                    } catch (e: MalformedInputException) {
                        message.edit(error(client,
                            event,
                            "Unable to parse text from attached file. You must only upload text documents.",
                            commandInfo))
                            .thenAccept { this@coroutineScope.launch { onComplete(it, client, true) } }
                        return@launch
                    }
                }
                if (args.isEmpty() && event.message.attachments.isEmpty()) {
                    message.edit(error(client,
                        event,
                        "You must have at least one file attached OR other text written after the command.",
                        commandInfo))
                        .thenAccept { this@coroutineScope.launch { onComplete(it, client, true) } }
                    return@launch
                }
                val response = normalHTTPClient.post("https://www.toptal.com/developers/hastebin/documents") {
                    setBody(body)
                }
                val haste = json.decodeFromString<HasteResponse>(response.bodyAsText())
                message.edit(MessageBuilder().appendLine("https://www.toptal.com/developers/hastebin/${haste.key}")
                    .build()).thenAccept { message ->
                    this.launch {
                        onComplete(message, client, client.config.auto_delete.bot.content_generation)
                    }
                }
            }
        }
    }
}