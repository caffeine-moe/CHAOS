package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.models.channels.DMChannel
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
    override var relationships : ClientUserRelationships,
    override var premium : Boolean,
    override var token : String,
    override val client : Client,
) : BaseClientUser {

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

    var _privateChannels = HashMap<String, DMChannel>()
    override val privateChannels : Map<String, DMChannel>
        get() = _privateChannels

    var _guilds = HashMap<String, Guild>()
    override val guilds : Map<String, Guild>
        get() = _guilds

}