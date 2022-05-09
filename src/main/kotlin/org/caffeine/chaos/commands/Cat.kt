package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.normalHTTPClient

@kotlinx.serialization.Serializable
private data class CatResponse(
    val file: String
)

suspend fun cat(client: Client, event: MessageCreateEvent) = coroutineScope{
    if (event.message.content == "${client.config.prefix}cat" || event.message.content == "${client.config.prefix}meow"){
        val response = json.decodeFromString<CatResponse>(normalHTTPClient.get("http://aws.random.cat/meow").bodyAsText())
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("**Meow!!**")
                .appendLine(response.file)
                .build(), client
        ).thenAccept { message ->
                this.launch { onComplete(message, client, client.config.auto_delete.bot.content_generation) }
        }
    }
}