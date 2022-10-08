package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.normalHTTPClient
import org.caffeine.chaos.config

class Upload :
    Command(arrayOf("upload"), CommandInfo("Upload", "upload <attachment.ext>", "Uploads a file to 0x0.st.")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        if (event.message.attachments.isEmpty()) {
            event.message.channel.sendMessage(error(client, event, "Message has no attachments!", commandInfo))
                .await().also { message -> onComplete(message, true) }
            return
        }
        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("Uploading...")
                .build()
        ).await().also { message ->
            val attachmentUrl = event.message.attachments.values.first().url
            val rsp = normalHTTPClient.request("https://0x0.st") {
                method = HttpMethod.Post
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("url", attachmentUrl)
                        }
                    )
                )
            }
            message.edit(
                MessageBuilder()
                    .appendLine(rsp.bodyAsText()).build()
            ).await().also { message ->
                onComplete(message, config.auto_delete.bot.content_generation)
            }
        }
    }
}
