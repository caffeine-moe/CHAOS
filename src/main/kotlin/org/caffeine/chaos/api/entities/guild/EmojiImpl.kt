package org.caffeine.chaos.api.entities.guild

import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake

data class EmojiImpl(
    override val client : Client,
    override val animated : Boolean,
    override val id : Snowflake,
    override val name : String,
) : Emoji {
    override val url : String
        get() = if (animated) "https://cdn.discordapp.com/emojis/${id}.gif?size=48" else "https://cdn.discordapp.com/emojis/${id}.png?size=48"

    var guildId = Snowflake(0)
    override var guild : Guild
        get() = runBlocking(client.coroutineContext) { client.user.fetchGuild(guildId) }
        set(value) {
            guildId = value.id
        }
}