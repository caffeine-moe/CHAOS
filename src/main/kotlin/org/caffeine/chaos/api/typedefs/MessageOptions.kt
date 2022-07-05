package org.caffeine.chaos.api.typedefs

@kotlinx.serialization.Serializable
data class MessageOptions (
    val content: String = "",
    val tts: Boolean = false,
    val nonce : String = ""
    //val embed: MessageEmbed;
)

interface MessageDeleteOptions {
    val timeout: Number?;
    val reason: String?;
}