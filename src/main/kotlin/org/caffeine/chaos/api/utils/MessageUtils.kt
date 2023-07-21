package org.caffeine.chaos.api.utils

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.*
import org.caffeine.chaos.api.entities.message.PartialAttachment
import java.io.File

interface MessageData {
    val content : String
    val tts : Boolean
    val nonce : String
    // val embed: MessageEmbed;
}

interface MessageSendData : MessageData {

    var attachments : List<PartialAttachment>

    @Transient
    var byteAttachments : HashMap<String, ByteArray>
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
) : MessageData

@Serializable
class MessageBuilder : MessageSendData {

    override var tts = false

    override var content : String = ""
    override val nonce : String get() = calcNonce()
    override var attachments : List<PartialAttachment> = emptyList()

    @Transient
    override var byteAttachments : HashMap<String, ByteArray> = hashMapOf()


    fun append(text : String) : MessageBuilder {
        content += text
        return this
    }

    fun appendLine(text : String) : MessageBuilder {
        content += "\n$text"
        return this
    }

    fun addAttachment(attachment : File) : MessageBuilder {
        byteAttachments[attachment.name] = attachment.readBytes()
        return this
    }

    suspend fun addAttachmentFromURL(attachment : String) : MessageBuilder {
        val url = URLBuilder(urlString = attachment).build()
        val body = normalHTTPClient.get(url).readBytes()
        byteAttachments[url.pathSegments.last().toString()] = body
        return this
    }

    fun addAttachment(attachment : ByteArray, name : String) : MessageBuilder {
        byteAttachments[name] = attachment
        return this
    }

}