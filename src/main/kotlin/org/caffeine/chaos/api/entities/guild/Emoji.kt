package org.caffeine.chaos.api.entities.guild

import org.caffeine.chaos.api.client.Client
import org.caffeine.chaos.api.entities.Snowflake

interface Emoji {
    val client : Client
    val animated : Boolean
    val id : Snowflake
    val guild : Guild
    val name : String
    val url : String
}