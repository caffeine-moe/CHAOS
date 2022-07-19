package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.models.Guild

class ClientUserImpl : BaseClientUser {
    private var _guilds = HashMap<String, Guild>()
    override val guilds get() = _guilds as Map<String, Guild>

    fun addGuild(guild : Guild) {
        _guilds[guild.id] = guild
    }

    fun removeGuild(guild : Guild) {
        _guilds.remove(guild.id)
    }

    fun setGuilds(guilds : Map<String, Guild>) {
        _guilds.clear()
        _guilds.putAll(guilds)
    }
}