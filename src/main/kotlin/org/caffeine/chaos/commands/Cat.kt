package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.json
import org.caffeine.chaos.api.typedefs.MessageBuilder
import org.caffeine.chaos.api.utils.normalHTTPClient
import org.caffeine.chaos.config

class Cat : Command(arrayOf("cat", "meow"), CommandInfo("Cat", "cat", "Sends a random cat from cataas.com.")) {

    @kotlinx.serialization.Serializable
    private data class CatResponse(
        val url : String,
    )

    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : MutableList<String>,
        cmd : String,
    ) {
        val response = normalHTTPClient.get("https://cataas.com/cat?json=true") {
            headers {
                append("Host", "cataas.com")
                append("Connection", "keep-alive")
                append("Pragma", "no-cache")
                append("Cache-Control", "no-cache")
                append("DNT", "1")
                append("Upgrade-Insecure-Requests", "1")
                append("Sec-GPC", "1")
                append("Sec-Fetch-Site", "same-origin")
                append("Sec-Fetch-Mode", "navigate")
                append("Sec-Fetch-User", "?1")
                append("Sec-Fetch-Dest", "document")
                append("Referer", "https://cataas.com/")
                append("Upgrade-Insecure-Requests", "1")
            }
        }
        val cat = json.decodeFromString<CatResponse>(response.bodyAsText())
        event.message.channel.sendMessage(
            MessageBuilder()
                .appendLine("**Meow!!**")
                .appendLine("https://cataas.com${cat.url}")
        ).await().map { message ->
            onComplete(message, config.auto_delete.bot.content_generation)
        }
    }
}
