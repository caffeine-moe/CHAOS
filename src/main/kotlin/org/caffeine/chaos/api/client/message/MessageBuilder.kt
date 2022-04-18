package org.caffeine.chaos.api.client.message

import org.caffeine.chaos.api.client.Client
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL

class MessageBuilder {
    private var sb = StringBuilder()
    private var attachments =  mutableListOf<MessageAttachment>()
    fun build(): Message {
        return Message(null, sb.toString(), attachments = attachments)
    }
    fun append(text: String) : MessageBuilder{
        sb.append(text)
        return this
    }
    fun appendLine(text: String) : MessageBuilder{
        sb.appendLine(text)
        return this
    }

    fun addAttachment(attachment: MessageAttachment): MessageBuilder {
        attachments.add(attachment)
        return this
    }
}