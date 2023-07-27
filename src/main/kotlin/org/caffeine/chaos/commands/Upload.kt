package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.awaitThen
import org.caffeine.chaos.api.utils.normalHTTPClient

class Upload :
    Command(
        arrayOf("upload"),
        CommandInfo("Upload", "upload <attachment.ext>", "Uploads a file to 0x0.st.")
    ) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        if (event.message.attachments.isEmpty()) {
            event.message.channel.sendMessage(error(client, event, "Message has no attachments!", commandInfo))
                .awaitThen { message ->
                    onComplete(message, true)
                }
            return
        }
        event.message.channel.sendMessage("Uploading...").awaitThen { message ->
            val attachmentUrl = event.message.attachments.values.first().url
            val rsp = normalHTTPClient.submitForm("https://0x0.st") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("url", attachmentUrl)
                        }
                    )
                )
            }
            message.edit(rsp.bodyAsText()).awaitThen { onComplete(it, true) }
        }
    }
}