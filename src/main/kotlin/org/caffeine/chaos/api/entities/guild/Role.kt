package org.caffeine.chaos.api.entities.guild

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake
import org.caffeine.chaos.api.typedefs.PermissionType

interface Role {
    val color : Int
    val hoisted : Boolean
    val icon : String?
    val id : Snowflake
    val managed : Boolean
    val mentionable : Boolean
    val name : String
    val permissions : List<PermissionType>
    val position : Int
    val unicodeEmoji : String?
    val guild : Guild
    val client : Client
}