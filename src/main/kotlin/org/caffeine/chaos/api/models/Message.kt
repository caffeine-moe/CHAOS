package org.caffeine.chaos.api.models

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.channels.TextChannel
import java.util.Date

data class Message(
    val client: Client = Client(),
    val id: String = "",
    val channel: TextChannel = TextChannel(),
    val guild: Guild? = Guild(),
    val author: User = User(),
    val member: Any? = Any(),
    val content: String = "",
    val timestamp: Date = Date(),
    val editedAt: Date = Date(),
    val tts: Boolean = false,
    val mentionedEveryone: Boolean = false,
    val nonce: String = "",
    val pinned: Boolean = false,
    val type: Number = 0,
)