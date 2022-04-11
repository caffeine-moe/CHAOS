package org.caffeine.chaos.api.client.message

@kotlinx.serialization.Serializable
data class MessageAuthor(
    val username: String,
    val discriminator: Int,
    val id: String,
    val avatar: String?
) {
    val discriminatedName = "$username#$discriminator"
}
