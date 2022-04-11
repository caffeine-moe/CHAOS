package org.caffeine.chaos.api.client.message

@kotlinx.serialization.Serializable
data class MessageSerialiser(
    var content: String,
    var nonce: String,
    var tts: Boolean = false
)