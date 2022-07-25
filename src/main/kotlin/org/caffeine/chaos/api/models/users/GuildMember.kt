package org.caffeine.chaos.api.models.users

import java.util.*

data class GuildMember(
    val id : String,
    val user : User,
    val nickname : String,
    //val _roles: Collection<string, Role>,
    val joinedAt : Date,
    val premiumSince : Date,
    val deaf : Boolean,
    val mute : Boolean,
)