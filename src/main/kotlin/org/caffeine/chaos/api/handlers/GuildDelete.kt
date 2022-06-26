package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.discord.client.Client
import org.caffeine.chaos.api.discord.client.ClientGuild
import org.caffeine.chaos.api.jsonc

@kotlinx.serialization.Serializable
private data class GuildDelete(
    val d : GuildDeleteD = GuildDeleteD(),
    val op : Int = 0,
    val s : Int = 0,
    val t : String = "",
)

@kotlinx.serialization.Serializable
private data class GuildDeleteD(
    val id : String = "",
)

fun guildDelete(payload : String, client : Client) {
    val parsed = jsonc.decodeFromString<GuildDelete>(payload)
    var guild = ClientGuild("", "")
    if (client.user.guilds.all {
            guild = it
            it.id == parsed.d.id
        }) {
        client.user.guilds.remove(guild)
    }
}