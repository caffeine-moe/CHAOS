package org.caffeine.chaos.api.entities.message.embeds

data class EmbedField(
    val name : String,
    val value : String,
    val inline : Boolean,
)