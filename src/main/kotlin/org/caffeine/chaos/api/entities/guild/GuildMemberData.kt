package org.caffeine.chaos.api.entities.guild

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.entities.users.User
import org.caffeine.chaos.api.typedefs.PermissionType

data class GuildMemberData (
    val nickname : String,
//    val roles : Map<Snowflake, Role>,
    val roleIds : List<Snowflake>,
//    val permissions : List<PermissionType>,
    val joinedAt : Snowflake,
    val premiumSince : Snowflake?,
    val deafened : Boolean,
    val muted : Boolean,
)