package org.caffeine.chaos.commands

import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.client.ClientEvent
import org.caffeine.chaos.api.utils.MessageBuilder
import org.caffeine.chaos.api.utils.awaitThen
import org.caffeine.chaos.api.utils.normalHTTPClient

class Cat : Command(arrayOf("cat", "meow"), CommandInfo("cat", "cat", "Sends a random cat image!")) {
    override suspend fun onCalled(
        client : Client,
        event : ClientEvent.MessageCreate,
        args : List<String>,
        cmd : String,
    ) {
        val cat = normalHTTPClient.get("https://cataas.com/cat").readBytes()
        event.channel.sendMessage(
            MessageBuilder()
                .appendLine("meow!")
                .addAttachment(cat, "cat.jpeg")
        ).awaitThen {
            onComplete(it, true)
        }
    }
}