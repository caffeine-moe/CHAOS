package org.caffeine.chaos.api.models.interfaces

interface DiscordUser {
    val username : String
    val discriminator : String
    val id : String
    val avatar : String?
    val discriminatedName : String
    fun avatarUrl() : String
}