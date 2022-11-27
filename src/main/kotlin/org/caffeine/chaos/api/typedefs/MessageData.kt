package org.caffeine.chaos.api.typedefs

import kotlinx.serialization.Serializable
import org.caffeine.chaos.api.utils.calcNonce

interface MessageData {
    val content : String
    val tts : Boolean
    val nonce : String
    // val embed: MessageEmbed;
}

interface MessageDeleteOptions {
    val timeout : Number?
    val reason : String?
}

@Serializable
class MessageBuilder : MessageData {

    override var tts = false

    override var content : String = ""
    override val nonce : String get() = calcNonce()

    //private var attachments = mutableListOf<MessageAttachment>()

    fun append(text : String) : MessageBuilder {
        content += text
        return this
    }

    fun appendLine(text : String) : MessageBuilder {
        content += if (content.isBlank()) text else "\n$text"
        return this
    }
    /*    fun addAttachment(attachment : MessageAttachment) : MessageBuilder {
                    attachments.add(attachment)
                    return this
        }*/
}