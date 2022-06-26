package org.caffeine.chaos.api.discord.message

class MessageBuilder {
    private var sb = StringBuilder()
    private var attachments = mutableListOf<MessageAttachment>()
    fun build() : Message {
        return Message("", sb.toString(), attachments = attachments)
    }

    fun append(text : String) : MessageBuilder {
        sb.append(text)
        return this
    }

    fun appendLine(text : String) : MessageBuilder {
        sb.appendLine(text)
        return this
    }

    fun addAttachment(attachment : MessageAttachment) : MessageBuilder {
        attachments.add(attachment)
        return this
    }
}