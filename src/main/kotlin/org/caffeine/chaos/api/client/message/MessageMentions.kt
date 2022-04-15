package org.caffeine.chaos.api.client.message

data class MessageMentions(
    val username: String,
    val discriminator: String,
    val id: String,
    val avatar: String
)
