package org.caffeine.chaos.api.client.user

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.models.Friend
import org.caffeine.chaos.api.models.Guild
import org.caffeine.chaos.api.models.interfaces.DiscordUser

interface BaseClientUser : DiscordUser {
    val verified : Boolean
    override val username : String
    override val discriminator : String
    override val id : String
    val email : String?
    val bio : String?
    val settings : ClientUserSettings
    override val avatar : String?
    val relationships : ClientUserRelationships
    //val channels: HashMap<String, BaseChannel>,
    val premium : Boolean
    val token : String
    val client : Client
    val guilds : Map<String, Guild>
}