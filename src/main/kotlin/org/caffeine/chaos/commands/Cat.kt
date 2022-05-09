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
import org.caffeine.chaos.api.ua

@kotlinx.serialization.Serializable
private data class CatResponse(
    val file: String
)

suspend fun cat(client: Client, event: MessageCreateEvent) = coroutineScope{
    if (event.message.content == "${client.config.prefix}cat" || event.message.content == "${client.config.prefix}meow"){
        val response = normalHTTPClient.get("http://aws.random.cat/meow"){
            headers {
                append("Host","aws.random.cat")
                append("Connection", "keep-alive")
                append("Pragma", "no-cache")
                append("Cache-Control", "no-cache")
                append("DNT", "1")
                append("Upgrade-Insecure-Requests", "1")
                append("User-Agent", ua)
                append("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                append("Sec-GPC", "1")
                append("Upgrade-Insecure-Requests", "1")
            }
        }
        val cat = json.decodeFromString<CatResponse>(response.bodyAsText())
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("**Meow!!**")
                .appendLine(cat.file)
                .build(), client
        ).thenAccept { message ->
                this.launch { onComplete(message, client, client.config.auto_delete.bot.content_generation) }
        }
    }
}