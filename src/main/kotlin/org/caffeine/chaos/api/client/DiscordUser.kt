package org.caffeine.chaos.api.client

import kotlinx.serialization.Transient
interface DiscordUser {
    @Transient
    val username : String
    @Transient
    val discriminator : String
    @Transient
    val id : String
    @Transient
    val avatar : String?
    @Transient
    val discriminatedName : String

    fun avatarUrl() : String {
        var av = "null"
        if (!avatar.isNullOrBlank()) {
            av = "https://cdn.discordapp.com/avatars/$id/$avatar?size=4096"
            if (avatar!!.startsWith("a_")) {
                av = "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            }
        }
        return av
    }
}