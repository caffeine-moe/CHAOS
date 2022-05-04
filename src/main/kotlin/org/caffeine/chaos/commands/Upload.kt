package org.caffeine.chaos.commands

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

suspend fun upload(client: Client, event: MessageCreateEvent) = coroutineScope {
    if (event.message.content.lowercase() == "${client.config.prefix}upload" && event.message.attachments.isNullOrEmpty()) {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("**Incorrect usage** '${event.message.content}'")
            .appendLine("**Error:** Message has no attachments!")
            .appendLine("**Correct usage:** `${client.config.prefix}upload (with attachment)`")
            .build(), client)
            .thenAccept { message -> this.launch { bot(message, client) } }
    }
    if (event.message.content.lowercase() == "${client.config.prefix}upload" && !event.message.attachments.isNullOrEmpty()) {
        event.channel.sendMessage(MessageBuilder()
            .appendLine("Uploading...")
            .build(), client).thenAccept { message ->
            this.launch {
                val attachmentUrl = event.message.attachments!!.first().url
                val rsp = HttpClient().request("https://0x0.st") {
                    method = HttpMethod.Post
                    setBody(MultiPartFormDataContent(
                        formData {
                            append("url", attachmentUrl)
                        }
                    ))
                }
                message.edit(MessageBuilder()
                    .appendLine(rsp.bodyAsText()).build(), client).thenAccept {
                    if (client.config.auto_delete.bot.content_generation) {
                        this.launch {
                            bot(it, client)
                        }
                    }
                }
            }
        }
    }
}