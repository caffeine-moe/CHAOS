package org.caffeine.chaos.api.client.message

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.caffeine.chaos.api.*
import org.caffeine.chaos.api.client.DiscordUserInfo

@Serializable
data class MessageMention(
    val username: String,
    val discriminator: String,
    val id: String,
    val avatar: String? = "",
) {
    val discriminatedName = "$username#$discriminator"
    fun avatarUrl(): String {
        var av = "null"
        if (!avatar.isNullOrBlank()) {
            av = "https://cdn.discordapp.com/avatars/$id/$avatar?size=4096"
            if (avatar.startsWith("a_")) {
                av = "https://cdn.discordapp.com/avatars/$id/$avatar.gif?size=4096"
            }
        }
        return av
    }

    suspend fun userInfo(): DiscordUserInfo {
        val re = discordHTTPClient.get("$BASE_URL/users/$id") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        val de = jsonc.decodeFromString<DiscordUserInfo>(re.bodyAsText())
        var ba = "null"
        if (!de.banner.isNullOrBlank()) {
            ba = "https://cdn.discordapp.com/banners/${id}/${de.banner}?size=1024"
            if (de.banner.startsWith("a_")) {
                ba = "https://cdn.discordapp.com/banners/${id}/${de.banner}.gif?size=1024"
            }
        }
        return DiscordUserInfo(
            de.accentColor,
            avatarUrl(),
            de.avatarDecoration,
            ba,
            de.bannerColor,
            de.bot,
            de.discriminator,
            de.id,
            de.publicFlags,
            de.username
        )
    }
}
