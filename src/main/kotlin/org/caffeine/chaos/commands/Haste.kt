package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvents
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.normalHTTPClient
import org.caffeine.chaos.config

class Haste : Command(
    arrayOf("haste"),
    CommandInfo("Haste", "haste <Text> OR <file.txt>", "Uploads text OR a text document to Hastebin.")
) {

    @kotlinx.serialization.Serializable
    private data class HasteResponse(
        val key : String,
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvents.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        event.channel.sendMessage(MessageBuilder().appendLine("Creating haste...").build()).await().also { message ->
            var body = ""
            if (args.isNotEmpty()) {
                body = args.joinToString(" ")
            }
            if (event.message.attachments.isNotEmpty()) {
                try {
                    body = normalHTTPClient.get(event.message.attachments.values.first().url).bodyAsText()
                } catch (e : MalformedInputException) {
                    message.edit(
                        error(
                            client,
                            event,
                            "Unable to parse text from attached file. You must only upload text documents.",
                            commandInfo
                        )
                    )
                        .await().also { onComplete(it, true) }
                    return
                }
            }
            if (args.isEmpty() && event.message.attachments.isEmpty()) {
                message.edit(
                    error(
                        client,
                        event,
                        "You must have at least one file attached OR other text written after the command.",
                        commandInfo
                    )
                )
                    .await().also { onComplete(it, true) }
                return
            }
            val response = normalHTTPClient.post("https://www.toptal.com/developers/hastebin/documents") {
                setBody(body)
            }
            val haste = json.decodeFromString<HasteResponse>(response.bodyAsText())
            message.edit(
                MessageBuilder().appendLine("https://www.toptal.com/developers/hastebin/${haste.key}")
                    .build()
            ).await().also { message ->
                onComplete(message, config.auto_delete.bot.content_generation)
            }
        }
    }
}
