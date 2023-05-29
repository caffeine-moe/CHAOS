package org.caffeine.chaos.api.entities.guild

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.PermissionType

interface GuildMember {
    val id : Snowflake
    val user : User
    val nickname : String
    val roles : Map<Snowflake, Role>
    val permissions : List<PermissionType>
    val joinedAt : Snowflake
    val premiumSince : Snowflake?
    val deafened : Boolean
    val muted : Boolean
    val guild : Guild
    val client : Client
}