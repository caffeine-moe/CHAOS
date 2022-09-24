package org.caffeine.chaos.api.models.interfaces

interface DiscordUser {
    val username : String
    val discriminator : String
    val discriminatedName : String
    val id : String
    val avatar : String?
    val bot : Boolean
    fun avatarUrl() : String
}