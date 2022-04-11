package org.caffeine.chaos.api.client.message

import io.ktor.http.*
import org.caffeine.chaos.Config
import org.caffeine.chaos.api.handlers.messagecreate
import java.util.concurrent.CompletableFuture

class MessageBuilder {
    private var sb = StringBuilder()
    fun build() : Message {
        var message = Message()
        message.content = sb.toString()
        return message
    }
    fun append(text: String) : MessageBuilder{
        sb.append(text)
        return this
    }
    fun appendLine(text: String) : MessageBuilder{
        sb.appendLine(text)
        return this
    }

}