package org.caffeine.chaos.api.entities.guild

import kotlinx.coroutines.runBlocking
import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.typedefs.PermissionType

data class RoleImpl(
    override var client : Client,
    override val color : Int = 0,
    override val hoisted : Boolean = false,
    override val icon : String? = "",
    override val id : Snowflake = Snowflake(0),
    override val managed : Boolean = false,
    override val mentionable : Boolean = false,
    override val name : String = "",
    override val permissions : List<PermissionType> = emptyList(),
    override val position : Int = 0,
    override val unicodeEmoji : String? = "",
) : Role {

    var guildId = Snowflake(0)
    override var guild : Guild
        get() = runBlocking(client.coroutineContext) { client.user.fetchGuild(guildId) }
        set(value) {
            guildId = value.id
        }
}