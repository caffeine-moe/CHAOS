package org.caffeine.chaos.api.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.asSnowflake
import org.caffeine.chaos.api.entities.message.MessageAttachment

interface MessageData {
    val content : String
    val tts : Boolean
    val nonce : String
    val attachments : Map<Snowflake, MessageAttachment>
    // val embed: MessageEmbed;
}

@Serializable
data class MessageReference(
    @Transient
    val guild_id : String? = null,
    val channel_id : String,
    val message_id : String,
)

@Serializable
data class MessageReply(
    override val content : String,
    override val tts : Boolean,
    override val nonce : String,
    @SerialName("message_reference")
    val messageReference : MessageReference,
) : MessageData {
    override val attachments : Map<Snowflake, MessageAttachment> = emptyMap()
}


@Serializable
class MessageBuilder : MessageData {

    override var tts = false

    override var content : String = ""
    override val nonce : String get() = calcNonce()

    override var attachments : HashMap<Snowflake, MessageAttachment> = hashMapOf()

    fun append(text : String) : MessageBuilder {
        content += text
        return this
    }

    fun appendLine(text : String) : MessageBuilder {
        content += if (content.isBlank()) text else "\n$text"
        return this
    }

    fun addAttachment(attachment : MessageAttachment) : MessageBuilder {
        attachments[attachment.id.asSnowflake()] = attachment
        return this
    }
}