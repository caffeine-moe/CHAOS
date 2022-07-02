package org.caffeine.chaos.commands

import kotlinx.coroutines.coroutineScope
import org.caffeine.chaos.Command
import org.caffeine.chaos.CommandInfo
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.message.MessageCreateEvent

class Cat : Command(arrayOf("cat", "meow"), CommandInfo("Cat", "cat", "Sends a random cat from cataas.com.")) {

    @kotlinx.serialization.Serializable
    private data class CatResponse(
        val url : String,
    )

    override suspend fun onCalled(
        client : Client,
        event : MessageCreateEvent,
        args : MutableList<String>,
        cmd : String,
    ) : Unit = coroutineScope {
/*        val response = normalHTTPClient.get("https://cataas.com/cat?json=true") {
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
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("**Meow!!**")
                .appendLine("https://cataas.com${cat.url}")
                .build()
        ).thenAccept { message ->
            this.launch { onComplete(message, client, client.config.auto_delete.bot.content_generation) }
        }*/
    }
}