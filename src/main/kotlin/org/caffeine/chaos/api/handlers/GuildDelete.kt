package org.caffeine.chaos.api.handlers

import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.jsonc
import org.caffeine.chaos.api.models.Guild

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
    var guild = Guild("", "")
/*    if (client.user.guilds.all {
            guild = it
            it.id == parsed.d.id
        }) {
        client.user.guilds.remove(guild)
    }*/
}