package org.caffeine.chaos.commands

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageBuilder
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Upload :
    Command(arrayOf("upload"), CommandInfo("Upload", "upload <attachment.ext>", "Uploads a file to 0x0.st.")) {
    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) =
        coroutineScope {
/*            if (event.message.attachments.isEmpty()) {
                event.channel.sendMessage(error(client, event, "Message has no attachments!", commandInfo))
                    .thenAccept { message -> this.launch { onComplete(message, client, true) } }
                return@coroutineScope
            }
            event.channel.sendMessage(MessageBuilder()
                .appendLine("Uploading...")
                .build()).thenAccept { message ->
                this.launch {
                    val attachmentUrl = event.message.attachments.first().url
                    val rsp = HttpClient().request("https://0x0.st") {
                        method = HttpMethod.Post
                        setBody(MultiPartFormDataContent(
                            formData {
                                append("url", attachmentUrl)
                            }
                        ))
                    }
                    message.edit(MessageBuilder()
                        .appendLine(rsp.bodyAsText()).build()).thenAccept { message ->
                        this.launch {
                            onComplete(message, client, client.config.auto_delete.bot.content_generation)
                        }
                    }
                }
            }*/
        }
}