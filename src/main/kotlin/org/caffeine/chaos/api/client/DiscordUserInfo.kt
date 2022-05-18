package org.caffeine.chaos.api.client

@kotlinx.serialization.Serializable
data class DiscordUserInfo(
    val accentColor: String? = "null",
    val avatar: String = "",
    val avatarDecoration: String? = "null",
    val banner: String? = "",
    val bannerColor: String? = "null",
    val bot: Boolean = false,
    val discriminator: String = "",
    val id: String = "",
    val publicFlags: Int = 0,
    val username: String = "",
)