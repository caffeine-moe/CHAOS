package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.Guild
import kotlin.math.absoluteValue

data class ClientUserImpl(
    override var verified : Boolean,
    override var username : String,
    override var discriminator : String,
    override var id : String,
    override var email : String?,
    override var bio : String?,
    override var settings : ClientUserSettings,
    override var avatar : String?,
    override val relationships : ClientUserRelationships,
    //val channels: HashMap<String, BaseChannel>
    override var premium : Boolean,
    override var token : String,
    override val client : Client,
): BaseClientUser {

    override val discriminatedName : String
        get() = "$username#$discriminator"

    override fun avatarUrl() : String {
        return if (!avatar.isNullOrBlank()) {
            if (avatar!!.startsWith("a_")) {
                "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            } else {
                "https://cdn.discordapp.com/avatars/$id/$avatar.png?size=4096"
            }
        } else {
            "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt().absoluteValue % 5}.png"
        }
    }

    private var _guilds = HashMap<String, Guild>()
    override val guilds : Map<String, Guild> get() = _guilds

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