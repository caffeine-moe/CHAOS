package org.caffeine.chaos.api.models.interfaces

import kotlinx.serialization.Transient

interface IDiscordUser {
    val username : String
    val discriminator : String
    val id : String
    val avatar : String?
    val discriminatedName : String
    fun avatarUrl() : String
}