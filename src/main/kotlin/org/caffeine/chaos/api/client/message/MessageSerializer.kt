package org.caffeine.chaos.api.client.message

@kotlinx.serialization.Serializable
data class MessageSerializer(
    var content : String,
    var nonce : String,
    var tts : Boolean = false,
)