package org.caffeine.chaos.api.client.message

import org.caffeine.chaos.Config
import org.caffeine.chaos.api.client.Client
import java.util.concurrent.CompletableFuture

class MessageChannel(var id: String) {
    suspend fun sendMessage(message: Message, config: Config, client: Client): CompletableFuture<Message> {
        return client.sendMessage(id, message, config)
    }
}